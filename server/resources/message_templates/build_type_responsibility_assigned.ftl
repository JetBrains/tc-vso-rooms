<#-- Uses FreeMarker template syntax, template guide can be found at http://freemarker.org/docs/dgui.html -->

<#import "common.ftl" as common>
<#import "responsibility.ftl" as resp>

<#global message>
${responsibleUser} is assigned for investigation of a build configuration failure.
${project.fullName}::${buildType.name}, assigned by ${reporterUser}
<@resp.removeMethod responsibility/>
<@resp.comment responsibility/>
${link.buildTypeConfigLink}
</#global>
