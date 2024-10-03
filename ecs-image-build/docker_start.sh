#!/bin/bash
#
# Start script for company-lookup-web


PORT=8080

exec java -jar -Dserver.port="${PORT}" "company-lookup.web.ch.gov.uk.jar"
