<#-- Uses FreeMarker template syntax, template guide can be found at http://freemarker.org/docs/dgui.html -->

<#import "common.ftl" as common>
<#import "responsibility.ftl" as resp>

<#global message>${responsibleUser} is assigned for build problem investigation in ${project.fullName}:
${buildProblems?first},

assigned by ${reporterUser}

<@resp.removeMethod responsibility/>
<@resp.comment responsibility/>
${link.myResponsibilitiesLink}</#global>