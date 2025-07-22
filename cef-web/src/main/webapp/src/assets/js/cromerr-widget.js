/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
/*
 * Custom JS function to load the Cromerr Widget
 */
function initCromerrWidget(userRoleId, token, baseServiceUrl, emissionsReportId, mfrId, toastr, emitter, userFeedbackEnabled, feedbackSubmitted, isSemiannual){

    var blockUIJS = "assets/js/jquery.blockUI.js";
    var cromerrJS="/Content/CromerrWidget/cromerrWidget.webpack.min.js";
    var jqueryJS="assets/js/jquery-3.5.1.min.js";

    if (checkIfScriptExists(baseServiceUrl+cromerrJS)) {
        initializeCromerrWidget(userRoleId, token, baseServiceUrl, emissionsReportId, mfrId, toastr, emitter, userFeedbackEnabled, feedbackSubmitted, isSemiannual);
    } else {
        var jqueryScript=loadScript(jqueryJS);
        jqueryScript.onload=function(){
            var blockUIScript=loadScript(blockUIJS);
            blockUIScript.onload=function(){
                var cromerrScript=loadScript(baseServiceUrl+cromerrJS);
                cromerrScript.onload=function(){
                    initializeCromerrWidget(userRoleId, token, baseServiceUrl, emissionsReportId, mfrId, toastr, emitter, userFeedbackEnabled, feedbackSubmitted, isSemiannual);
                }
                cromerrScript.setAttribute('id', "cromerrServerSign");
            }
        }
    }
}


function loadScript(srciptUrl){
    var script = document.createElement("script");
    document.body.insertBefore(script, this.firstChild);
    script.setAttribute('src', srciptUrl);
    return script;
}

function initializeCromerrWidget(userId, userRoleId, baseServiceUrl, emissionsReportId, mfrId, toastr, emitter, feedbackEnabled, feedbackSubmitted, isSemiannual){
    $( document ).ready(function() {
        $cromerrWidget.initializeCromerrWidget({
            esignButtonId : "certifyAndSubmit",
            widgetParams : {
                dataflow : "CAER",
                userId : userId,
                userRoleId : userRoleId,
                disclaimerText: 'I certify under penalty of law that I have personally examined and am familiar with the information I submitted in this and all attached documents, and that based on my inquiry of those individuals immediately responsible for obtaining the information, I believe that the submitted information is true, accurate, and complete. I am aware that there are significant penalties for submitting false information, including the possibility of fine and imprisonment.',
                props : {
                    activityDescription : 'Combined Air Emissions Reporting Submission'
                }
            },
            success : function(event) {
                $.blockUI();
                $.ajax({
                      url: "api/emissionsReport/submitToCromerr",
                      type: "get", //send it through get method
                      data: {
                        activityId: event.activityId,
                        reportId: emissionsReportId,
                        isSemiannual: isSemiannual
                      },
                      success: function(response) {
                        //if feedback is enabled and the user hasn't already submitted feedback for this report go to feedback page after signing
                        if (feedbackEnabled && !feedbackSubmitted) {
                            toastr.success('', "The Emission Report has been successfully electronically signed and submitted to the agency for review.");
                            $.unblockUI();
                            window.location.href="/cef-web/#/facility/"+mfrId+"/report/"+emissionsReportId+"/userfeedback";
                        } else {
                            toastr.success('', "The Emission Report has been successfully electronically signed and submitted to the agency for review.");
                            $.unblockUI();
                            window.location.href="/cef-web/#/facility/"+mfrId+"/report";
                        }
                      },
                      error: function(xhr) {
                          $.unblockUI();
                          toastr.error('', "There was an error electronically signing your emission report. Please try again.");
                      }
                    });
            },
            error : function(error) {
                toastr.error('', "There was an error electronically signing your emission report. Please try again.");
            },
            cancel: function() {
            }
        });
        emitter.next(true);
    });

}

function checkIfScriptExists(url){
    var scripts = document.getElementsByTagName('script');
    for (var i = scripts.length; i--;) {
        if (scripts[i].src == url) return true;
    }
    return false;
}
