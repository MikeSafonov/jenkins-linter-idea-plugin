# jenkins-linter-idea-plugin Changelog

## [Unreleased]
## [0.5.3]
## [0.5.2]
## [0.5.2]

### Fixes

- Support for 2021.2 by [@sercheo87](https://github.com/sercheo87)

## [0.5.1]

### Fixes

- fixed ui update from `Task.Backgroundable`

## [0.5.0]
### Features

- builtin SSL and proxy support

### Improvement

- linting process wrapped inside `Task.Backgroundable`
- UI for linting errors

### Fixes

- build `pipeline-model-converter/validate` URL relative to settings `Jenkins url`

## [0.4.0]
### Improvement

- changed `pluginSinceBuild` to 201 and `pluginUntilBuild` to 211.*

## [0.3.0]
### Features

- add `Use crumb issuer` setting to prevent accessing crumb

## [0.2.0]
### Features

- add Jenkins authorization

## [0.1.1]
### Improvement

- add a meaningful message when no host provided
- add trustSelfSigned settings option

## [0.1.0]
### Added

- initial implementation of Jenkins Pipeline linting process.