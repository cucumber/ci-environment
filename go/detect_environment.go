package cienvironment

import (
	"bufio"
	"bytes"
	_ "embed"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"regexp"
	"strings"
)

//go:embed CiEnvironments.json
var ciEnvironmentTemplates string

func DetectCIEnvironment() *CiEnvironment {
	ciEnvironments := []*CiEnvironment{}

	// The Azure template currently only has single back-slashes to escape the slashes in the variable/pattern/replacement
	// patterns	"${BUILD_SOURCEBRANCH/refs\/heads\/(.*)/\\1}" where others are like "${CI_PULL_REQUEST/(.*)\\/pull\\/\\d+/\\1.git}".
	// To make these all consistant prior to parsing, we'll replace those with double back-slashes prior to processing.
	re := regexp.MustCompile(`([^\\])\\/`)
	template := re.ReplaceAllString(ciEnvironmentTemplates, `${1}\\/`)
	err := json.Unmarshal([]byte(template), &ciEnvironments)

	if err != nil {
		l := log.New(os.Stderr, "", 0)
		l.Printf("error parsing ci templates %s", err)
		return nil
	}
	var environment *CiEnvironment = nil
	for _, ciEnvironment := range ciEnvironments {

		// evaluate the expressions for each part of the ci environment configuration
		ciEnvironment.URL, _ = evalutate(ciEnvironment.URL)
		ciEnvironment.BuildNumber, _ = evalutate(ciEnvironment.BuildNumber)
		ciEnvironment.Git.Branch, _ = evalutate(ciEnvironment.Git.Branch)
		ciEnvironment.Git.Revision, _ = evalutate(ciEnvironment.Git.Revision)
		ciEnvironment.Git.Remote, _ = evalutate(ciEnvironment.Git.Remote)
		ciEnvironment.Git.Tag, _ = evalutate(ciEnvironment.Git.Tag)

		if ciEnvironment.IsPresent() {
			environment = ciEnvironment
			break
		}
	}
	if environment == nil {
		return nil
	}
	return environment.SanitizeGit()
}

// evaluate each token parsed from the expression and create a value by concatenating all evaluated tokens.
func evalutate(expression string) (string, error) {
	result := ""
	sc := NewScanner(expression)
	tokens := sc.ReadTokens()
	for _, token := range tokens {
		switch t := token.(type) {
		case *variableExpression:
			s, err := t.Evaluate()
			if err != nil {
				return result, err
			}
			result = fmt.Sprintf("%s%s", result, s)
		case *literalExpression:
			result = fmt.Sprintf("%s%s", result, t.Value())
		}
	}
	return result, nil
}

// any literal or variable expression
type configValue interface {
	Value() string
}

type variableExpression struct {
	value string
}

func (v *variableExpression) Value() string {
	return v.value
}

func (v *variableExpression) Evaluate() (string, error) {
	tokens := []string{}
	var buf bytes.Buffer
	prev := eof

	for _, ch := range v.value {
		if ch == '/' && prev != '\\' {
			tokens = append(tokens, buf.String())
			buf.Reset()
		} else {
			buf.WriteRune(ch)
		}
		prev = ch
	}
	tokens = append(tokens, buf.String())
	value := ""
	matches := [][]string{}
	for i, token := range tokens {
		switch i {
		case 0:
			value = getEnv(token)
			if len(value) == 0 {
				return "", fmt.Errorf("undefined variable: %s", token)
			}
		case 1:
			re, err := regexp.Compile(token)
			if err != nil {
				return value, fmt.Errorf("error compiling regex: %s", err)
			}
			matches = re.FindAllStringSubmatch(value, -1)
		case 2:
			// if no matches exist, then this expression has no value
			if len(matches) == 0 {
				value = ""
			}
			for _, match := range matches {
				for groupIdx, group := range match {
					if groupIdx == 0 {
						continue
					}
					placeHolder := fmt.Sprintf("\\%d", groupIdx)
					value = strings.Replace(token, placeHolder, group, -1)
				}
			}
		}
	}

	return value, nil
}

// getEnv return the environment variable if no wildcard is included in the variable name. If the variable name
// does contain a wildcard, then convert to a valid regex and check all envrionemtn variables until we find one that
// matches, if a match does exist.
func getEnv(name string) string {
	if strings.Contains(name, "*") {
		re := regexp.MustCompile(strings.Replace(name, "*", ".*", -1))
		for _, element := range os.Environ() {
			variable := strings.Split(element, "=")
			if re.MatchString(variable[0]) {
				return variable[1]
			}
		}
	}
	return os.Getenv(name)
}

type literalExpression struct {
	value string
}

func (l *literalExpression) Value() string {
	return l.value
}

type Scanner struct {
	r *bufio.Reader
}

func NewScanner(expression string) Scanner {
	reader := bufio.NewReader(strings.NewReader(expression))
	return Scanner{
		r: reader,
	}
}

var eof = rune(0)

// ReadTokens returns an array of all literal and variable tokens found in the expression being scanned
func (s Scanner) ReadTokens() []configValue {
	tokens := []configValue{}
	for {
		token := s.Next()
		if token == nil {
			break
		}
		tokens = append(tokens, token)
	}
	return tokens
}

// Next returns the next token from the scanner
func (s Scanner) Next() configValue {
	ch := s.read()
	if ch == eof {
		return nil
	}
	switch ch {
	case '$':
		peek := s.read()
		if peek == '{' {
			return s.readVariableExpression()
		} else {
			s.unread()
			return s.readLiteral()
		}
	}
	s.unread()
	return s.readLiteral()
}

func (s Scanner) readVariableExpression() *variableExpression {
	expression := &variableExpression{}
	var buf bytes.Buffer
	for {
		if ch := s.read(); ch == eof || ch == '}' {
			expression.value = buf.String()
			break
		} else {
			buf.WriteRune(ch)
		}
	}
	return expression
}

func (s Scanner) readLiteral() *literalExpression {
	literal := &literalExpression{}
	var buf bytes.Buffer
	for {
		if ch := s.read(); ch == eof {
			literal.value = buf.String()
			break
		} else if ch == '$' {
			s.unread()
			literal.value = buf.String()
			break
		} else {
			buf.WriteRune(ch)
		}
	}
	return literal
}

func (s Scanner) read() rune {
	ch, _, err := s.r.ReadRune()
	if err != nil {
		return eof
	}
	return ch
}

func (s Scanner) unread() {
	s.r.UnreadRune()
}
