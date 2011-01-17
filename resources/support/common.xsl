<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- 
Copyright 2011 Procippus, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />
	
	<xsl:param name="timestamp" />

	<xsl:template name="header">
		<xsl:text>
		<script type="text/javascript" src="/assets/js/jquery-1.4.2.min.js">
			<xsl:comment>js include</xsl:comment>	
		</script>
		<script type="text/javascript" src="/assets/js/jquery-ui-1.8.6.custom.min.js">
			<xsl:comment>js include</xsl:comment>
		</script>
		<link type="text/css" href="/assets/css/cupertino/jquery-ui-1.8.6.custom.css" rel="stylesheet" />
		<style type="text/css">
			body{ font: 72.5% "Trebuchet MS", sans-serif; margin-left: 50px; margin-right: 50px; margin-top:
			10px; }
			li { margin-bottom: 3px; }

			.content { margin-left: 5px; margin-right: 5px; }
			.title { font-size: 1.6em; margin-bottom: 5px; }
			.subtitle { font-size: 1.2em; margin-bottom: 3px; color: #999;}
			.header { height: 45px; margin-bottom: 5px;}
			.header .title { font-size: 2em; color: #999; }
			.footerWrapper { margin-top: 20px; }
			.footer { margin-bottom: 5px; font-size: 8pt;}
			.footer a { color: #333; }
			.disclaimer { color: #999; font-size: 8pt; font-style: italic; }
			.bread-crumbs { font-size: 10pt; color: #666;
			margin-bottom: 15px; border-bottom: 1px solid #ccc;}
			.bread-crumbs a { font-size: 10pt; color: #666; }
			.description { margin-top: 5px; margin-bottom: 15px; }
		</style>
		<script type="text/javascript">
			$(function(){
			// Dialog
			$('#license').dialog({
			autoOpen: false,
			width: 600,
			height: 260,
			buttons: {
			"Ok": function() {
			$(this).dialog("close");
			}
			}
			});

			// Dialog Link
			$('#license_link').click(function(){
			$('#license').dialog('open');
			return false;
			});

			});
		</script>
		</xsl:text>
	</xsl:template>

	<xsl:template name="banner">
		<div class="header ui-accordion-header ui-helper-reset ui-state-default ui-corner-all" style="height: 60px;">
			<div class="title" style="margin-top: 7px; margin-left: 5px;">Ivy Facade</div>
			<div class="subtitle" style="margin-top: -3px; margin-left: 40px;">Visualize your dependencies</div>
		</div>
	</xsl:template>

	<xsl:template name="footer">
		<xsl:text disable-output-escaping="yes">
<div class="footerWrapper">
	<div class="footer ui-tabs ui-widget ui-widget-content ui-corner-all">
		<a href="#" id="license_link"><span class="ui-icon ui-icon-newwin" style="float: left;"><xsl:comment>icon</xsl:comment></span>Ivy Facade By: Procippus, LLC.</a>
	</div>
	<div class="disclaimer">**Apache Ivy, Apache Ant, Ivy, Ant, Apache, the Apache Ivy logo, the Apache Ant logo and the Apache feather logo are trademarks of The Apache Software Foundation. All other marks mentioned may be trademarks or registered trademarks of their respective owners.</div>
</div>
<div id="license">
	   <div>Copyright 2011 <a href="http://www.procippus.com" target="blank">Procippus, LLC</a></div>
	   <div>Licensed under the Apache License, Version 2.0 (the "License");
	   you may not use this file except in compliance with the License.
	   You may obtain a copy of the License at</div>
		<blockquote>
	       <a href="http://www.apache.org/licenses/LICENSE-2.0">http://www.apache.org/licenses/LICENSE-2.0</a>
			</blockquote>
			<div>Unless required by applicable law or agreed to in writing, software
		   distributed under the License is distributed on an "AS IS" BASIS,
		   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		   See the License for the specific language governing permissions and
		   limitations under the License.</div>
	  </div>
	  <br/>
	  <div style="text-align: right; margin-top: 5px;" class="disclaimer">Last updated: <xsl:value-of select="$timestamp" /></div>
</xsl:text>
	</xsl:template>

</xsl:stylesheet>