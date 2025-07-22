# CefWeb Cypress Testing

To start Cypress, you will want to run `./node_modules/.bin/cypress open` from the webapp directory of this project

## Running Locally

You will need to have your local instance of CAER running for Cypress to work locally. After CAER is running, make sure that the `environment` property in `cypress.json` is set to `local` and any Cypress tests that work locally should be functional. You will need to make sure Ford Motor Co doesn't have a 2020 year report and has the `ford_2019.json` file from the `fixtures` folder 
uploaded as a 2019 report.

## Running in DEV

Make sure that the `environment` property in `cypress.json` is set to `dev`. If Cypress is using Chrome as it's browser you might need to set the `chrome://flags/#same-site-by-default-cookies` and `chrome://flags/#cookies-without-same-site-must-be-secure` to disabled in the Cypress browser. Firefox appears to work by default at time of writing.

## Useful Information

We are able to automatically generate tests off of local environments using [Cypress Studio](https://docs.cypress.io/guides/core-concepts/cypress-studio#Using-Cypress-Studio). If you add a new test, it will initially prompt you for a URL and I recommend using `/` for that URL as it will then build off of the baseUrl specified in `cypress.json`.

`cy.visit()` does not work once logged into DEV, but works on local and can be used to simplify testing on local environments. If you are creating tests using Cypress Studio off of your local, I recommend either extending the previous test in the flow and then separating out the new logic manually OR creating a new test, navigating to the page that the previous test ended on, recording your new test and saving it, and then manually removing all of the steps that navigated to the starting point since it will remain there if the next test doesn't start with a visit. The full navigation can be left in for any tests designed exclusively for local use.

We should be able to change the order tags are used to generated selectors for Cypress Studio test generation by modifying the `support/index.js` file to prioritize `aria-label` highly, however that functionality is [currently broken](https://github.com/cypress-io/cypress/issues/7745) and using attribute instead will select random angular automatically generated attributes the majority of the time. Due to this, I have been adding `data-cy` attributes when I needed to select a specific element (normally using the same info as the aria-label so we can switch to using those if desired).