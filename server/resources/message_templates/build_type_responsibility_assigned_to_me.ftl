<#-- Uses FreeMarker template syntax, template guide can be found at http://freemarker.org/docs/dgui.html -->

<#import "common.ftl" as common>
<#import "responsibility.ftl" as resp>

<#global message>
${responsibility.responsibleUser.descriptiveName} is assigned for investigation of a build configuration failure.
${project.fullName}::${buildType.name}, assigned by ${responsibility.reporterUser.descriptiveName}
<@resp.removeMethod responsibility/>
<@resp.comment responsibility/>
${link.buildTypeConfigLink}
</#global>
