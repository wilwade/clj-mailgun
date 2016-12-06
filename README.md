# clj-mailgun

Simple command line tool to send json emails via Mailgun

## Installation

1. Get a [Mailgun Account](http://www.mailgun.com)
2. Install [leiningen](http://leiningen.org)
3. Clone repository
4. `cd clj-mailgun`

## Build

- `lein uberjar`

## Testing

- `lein test`

## Running

### Environment Variables (Required)
- `MAILGUN_API_KEY` API Key
- `MAILGUN_API_DOMAIN` Mailgun Domain

### Sending Basic Email
Create a JSON file
```javascript
{
  "to": "To Email Address",
  "from": "From Email Address",
  "subject": "Email Subject Line",
  "body": "HTML Contents"
}
```

- `lein run -- --json JSON_FILE`
- (Or if built)
- `java -jar ./target/uberjar/clj-mailgun-0.1.0-SNAPSHOT-standalone.jar --json JSON_FILE`

### Sending Templated Email
Create [mustache style templates](https://mustache.github.io/) and put them in the resources/templates directory.
#### Naming
- Basic text only email?
  - [template name].mustache
- Basic HTML only email?
  - [template name]-html.mustache
- Proper text and HTML email (BEST)?
  - [template name]-text.mustache
  - [template name]-html.mustache

Create a JSON file
```javascript
{
  "to": "To Email Address",
  "from": "From Email Address",
  "subject": "Email Subject Line",
  "template": "template name",
  "params": {
    "title": "Welcome!" //And other named variables
  }
}
```

- `lein run -- --json JSON_FILE`

Remember: It is your responsibility to include all required parameters for the template!

## License

Copyright © 2016 Wil Wade

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
