# clj-mailgun

Simple service emails via Mailgun

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

### Environment Variables (Optional)
- `LOG_DEBUG` Print debug level logs (default: false)
- `LOG_INFO` Print info level logs (default: true)
- `LOG_ERROR` Print error level logs (default: true)
- `SERVER_PORT` Port the server uses (default: 8081)
- `SERVER_IP` IP the server runs on (default: 0.0.0.0)

### Start Service

- `lein run`

## Using

### Sending Basic Email
Create a JSON request
curl -XPOST -H "Content-Type: application/json" "http://localhost:8081/send" -d
```javascript
{
  "to": "To Email Address",
  "from": "From Email Address",
  "subject": "Email Subject Line",
  "body": "HTML Contents"
}
```

### Idempotent Check
You can send any generated id to "/send/[id]" (or in the JSON) and the server will check to see if the id has already been used. (Resets when the service restarts)

Any requests that do not send an id, will receive a generated uuid in the response.

If a conflicting request is received, no email will be send and a 409 response code will be returned.

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

See "Sending Basic Email" except with
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

Remember: It is your responsibility to include all required parameters for the template!

## License

Copyright © 2016 Wil Wade

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
