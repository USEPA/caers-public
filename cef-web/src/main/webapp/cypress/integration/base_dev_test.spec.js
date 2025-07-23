// test1.spec.js created with Cypress
//
// Start writing your Cypress tests below!
// If you're unfamiliar with how Cypress works,
// check out the link below and learn how to write your first test:
// https://on.cypress.io/writing-first-test

describe('SAMPLE TEST', () => {

  before(function(){
    Cypress.Cookies.debug(true);

    cy.clearCookie('CDX2_ASP.NET_SessionId');
    cy.clearCookie('CDX2AUTH');
    cy.clearCookie('pubCDXSessionEnd');
    cy.clearCookie('XSRF-TOKEN');
    cy.clearCookie('JSESSIONID');

    Cypress.Cookies.defaults({
        preserve: ['CDX2_ASP.NET_SessionId', '.CDX2AUTH', 'pubCDXSessionEnd', 'XSRF-TOKEN', 'JSESSIONID']
    });
    
    cy.fixture('userLogin').then(function(user){
        this.user=user;
    });
  });

  Cypress.Commands.add('login', function(){
    if(Cypress.env('environment') == 'dev') {
      cy.visit(Cypress.env('dev_url'));
    } else if (Cypress.env('environment') == 'test') {
      cy.visit(Cypress.env('test_url'));
    } else if (Cypress.env('environment') == 'prod') {
      cy.visit(Cypress.env('prod_url'));
    }

    cy.get('#LoginUserId').type(this.user.userId);
    cy.get('#LoginPassword').type(this.user.password);

    cy.get('#LogInButton').click();  
    cy.get(':nth-child(1) > .mycdx-role > .handoff').click();
  });



  it('Handoff from CDX to CAER', function(){
    if(Cypress.env('environment') == 'local') {
      cy.visit('/');
    } else {
      cy.login();
    }
  });

  describe('EMPTY TEST', () => {

  });
})

