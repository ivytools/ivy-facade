<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Copyright 2011 Procippus, LLC Licensed under the Apache License, Version 
	2.0 (the "License"); you may not use this file except in compliance with 
	the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
	Unless required by applicable law or agreed to in writing, software distributed 
	under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
	OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
	the specific language governing permissions and limitations under the License. -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns="http://www.w3.org/1999/xhtml">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		indent="yes" />

	<xsl:import href="common.xsl" />

	<xsl:param name="FULL_PATH" />
	<xsl:param name="ORG" />
	<xsl:param name="MOD" />
	<xsl:param name="REV" />

	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
			<head>
				<title>Ivy Facade - Ivy Root</title>
				<xsl:call-template name="header" />
				<script type="text/javascript">
					$(function(){
					$('#tabs').tabs();
					});
 				</script>
			</head>
			<body>
				<xsl:call-template name="banner" />
				<div class="bread-crumbs">Home</div>
				<div class="content">
					<div id="ivysettings" style="margin-bottom: 10px;">
						Welcome, this is an IVY repository. Apache Ivy(tm) is a popular
						dependency manager focusing on flexibility and simplicity.
						Find out
						more about its unique enterprise features, what people say about
						it, and how it can improve your build system!
						<br />
						<br />
						<a href="http://ant.apache.org/ivy/index.html" target="_blank">Apache IVY
						</a>
						<br />
						<br />
						In order to use this Ivy repository, you may use the
						<a href="ivysettings.xml">Ivy settings</a>
						file.
						<br />
					</div>

					<div id="tabs">
						<ul>
							<li>
								<a href="#main">Organizations</a>
							</li>
							<li>
								<a href="#health">Repository Health</a>
							</li>
							<li>
								<a href="#index">Module Index</a>
							</li>
						</ul>
						<div id="main">
							<ul>
								<xsl:for-each select="//home/directories/directory">
									<xsl:param name="dirName" select="@name" />
									<li>
										<a href="{$dirName}">
											<xsl:value-of select="@name" />
										</a>
									</li>
								</xsl:for-each>
							</ul>
						</div>
						<div id="index">
							<ul>
								<xsl:for-each select="//home/index/dependency">
									<xsl:param name="indexDepLink"
										select="concat(concat(@org, '/' ,@name), '/', @rev)" />
									<li>
										<a href="{$indexDepLink}">
											<xsl:value-of select="@org" />
											/
											<xsl:value-of select="@name" />
											/
											<xsl:value-of select="@rev" />
										</a>
									</li>
								</xsl:for-each>
							</ul>
						</div>
						<div id="health">
							<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">Statistics:</div>
							<table>
								<tr>
									<td>
										<div>Total number of modules:</div>
									</td>
									<td>
										<div style="margin-left: 10px;">
											<xsl:value-of select="//home/health/moduleStats/@totalModules" />
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div>Modules with unsatisfied dependencies:</div>
									</td>
									<td>
										<div style="margin-left: 10px;">
										<xsl:value-of
											select="//home/health/moduleStats/@totalMissingDependencies" />
										</div>
									</td>
								</tr>
							</table>
							<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">Modules missing dependencies:</div>
							<ul>
								<xsl:for-each select="//home/health/missing/dependency">
									<xsl:param name="missingDepLink"
										select="concat(concat(@org, '/' ,@name), '/', @rev)" />
									<li>
										<a href="{$missingDepLink}">
											<xsl:value-of select="@org" />
											/
											<xsl:value-of select="@name" />
											/
											<xsl:value-of select="@rev" />
										</a>
									</li>
								</xsl:for-each>
							</ul>
						</div>

					</div>
				</div>
				<xsl:call-template name="footer" />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>