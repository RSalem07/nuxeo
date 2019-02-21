<@extends src="base.ftl">
    <@block name="title">Index 2</@block>
    <@block name="header">
        Price for the document of Id = ${documentId}
    </@block>
    <@block name="content">
        <#if price>
            Price: ${price}!
        <#else>
            No Price available for the document of Id = ${documentId}
        </#if>
    </@block>
    <@block name="footer">
        Thanks !
    </@block>
</@extends>