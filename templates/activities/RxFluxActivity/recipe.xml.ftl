<?xml version="1.0"?>
<#import "root://activities/common/kotlin_macros.ftl" as kt>
<recipe>

<merge from="root/AndroidManifest.xml.ftl"
	to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />

<merge from="root/res/values/strings.xml.ftl"
  	to="${escapeXmlAttribute(resOut)}/values/strings.xml" />

<instantiate from="root/src/app_package/RxFluxActivity.java.ftl"
              to="${escapeXmlAttribute(srcOut)}/${activityClass}.java" />
<open file="${escapeXmlAttribute(srcOut)}/${activityClass}.java" />

<instantiate from="root/src/app_package/RxFluxStore.java.ftl"
              to="${escapeXmlAttribute(srcOut)}/store/${storeClass}.java" />
<open file="${escapeXmlAttribute(srcOut)}/store/${storeClass}.java" />

<#if generateFragment>
  <#if isRxFluxFragment>
    <instantiate from="root/src/app_package/RxFluxFragment.java.ftl"
                  to="${escapeXmlAttribute(srcOut)}/${fragmentClass}.java" />
    <open file="${escapeXmlAttribute(srcOut)}/${fragmentClass}.java" />
  <#else>
    <instantiate from="root/src/app_package/Fragment.java.ftl"
                  to="${escapeXmlAttribute(srcOut)}/${fragmentClass}.java" />
    <open file="${escapeXmlAttribute(srcOut)}/${fragmentClass}.java" />
  </#if>
</#if>

<#if generateLayout>
    <instantiate from="root/res/layout/fragment.xml.ftl"
                  to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
    <open file="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
</#if>

</recipe>
