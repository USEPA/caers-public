/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If
 * not, see <https://www.gnu.org/licenses/>.
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
