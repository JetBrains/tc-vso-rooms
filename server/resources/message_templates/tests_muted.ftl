<#-- Uses FreeMarker template syntax, template guide can be found at http://freemarker.org/docs/dgui.html -->

<#import "common.ftl" as common>
<#import "mute.ftl" as mute>

<#global message><@mute.testInfo tests/> muted <@mute.inScope scopeBean/> by ${mutingUser}.

<@mute.comment muteInfo/>.
<@mute.unmute unmuteModeBean/>
${link.mutedProblemsLink}
</#global>
