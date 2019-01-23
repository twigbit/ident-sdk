# twigbit ident sdk
## Architecture considerations

The SDK consists of several modules:

| module | description |
| ------ | ----------- |
| core   | - bundles `com.governikus.ausweisapp` <br> - capability checks (nfc / nfc extended length support)<br>  - handle ident state machine |
| ui     | - drop-in ui |
| compat | - compat activity that wraps activity lifecycle |
| ausweisident | - AusweisIdentBuilder class for building AusweisIdent tcTokenUrls |

