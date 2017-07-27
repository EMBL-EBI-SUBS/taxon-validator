# Taxonomy Validator

[![Build Status](https://travis-ci.org/EMBL-EBI-SUBS/taxon-validator.svg?branch=master)](https://travis-ci.org/EMBL-EBI-SUBS/taxon-validator)

This validator consumes the [ENA Taxonomy Service](http://www.ebi.ac.uk/ena/browse/taxonomy-service) to programmaticaly get taxonomy entries by taxon ID.
We use a caching strategy to prevent repetitive calls and improve the processing speed. We do this using [Caffeine](https://github.com/ben-manes/caffeine/wiki) and our cache expires every 24h.

## License
This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details