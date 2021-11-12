# Contributing

Thank you for considering contributing to Cucumber create-meta!

## Code of Conduct

Everyone interacting in this codebase and issue tracker is expected to follow
the Cucumber [code of conduct](https://cucumber.io/conduct).

## Adding support for new CI servers

If you want to see support for a new CI server, please submit a pull request.

Here are the steps:

* Modify the file `ciDict.json`, using environment variables to extract information.
  See [ARCHITECTURE.md](./ARCHITECTURE.md#ci-definitions) for more information on
  `ciDict.json`.
* Add an approval test in `testdata/YourCi.txt` and `testdata/YourCi.txt.json`.

Then build and run the tests for all implementations:

    cd java && mvn test
    cd ../javascript && npm test
    cd ../ruby && bundle exec rspec

If all tests pass, commit your code and send us a pull request. Bonus points if you
also update `CHANGELOG.md` and `README.md`.

You might want to look at the source code for [danger](https://github.com/danger/danger/tree/master/lib/danger/ci_source)
to understand how various CI server environment variables should be interpreted.
