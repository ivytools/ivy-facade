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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" indent="yes"/>
	<xsl:import href="common.xsl" />
	
	<xsl:param name="FULL_PATH" />
	<xsl:param name="ORG" />
	<xsl:param name="MOD" />
	<xsl:param name="REV" />
	
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
			<head>
				<title>Ivy Facade - <xsl:value-of select="$ORG" /> <xsl:if test="$MOD"> / <xsl:value-of select="$MOD" />  </xsl:if> </title>
				<xsl:call-template name="header" />				
			</head>
			<body>
				<xsl:call-template name="banner" />
				<div class="bread-crumbs">
					<xsl:choose>
						<xsl:when test="$ORG and not($MOD)">
							<a href="../">Home</a> / 
						</xsl:when>
						<xsl:when test="$ORG and $MOD">
							<a href="../../">Home</a> / 
						</xsl:when>
						<xsl:otherwise>
							Home
						</xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="not($ORG)"></xsl:when>
						<xsl:otherwise>
							<a href="../"><xsl:value-of select="$ORG" /></a>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="$MOD">
						/ <xsl:value-of select="$MOD" />
					</xsl:if>
				</div>
				<div class="content">
					<div class="ui-tabs ui-widget ui-widget-content ui-corner-all">
						<xsl:choose>
							<xsl:when test="$ORG and not($MOD)">
								Modules: 
							</xsl:when>
							<xsl:when test="$ORG and $MOD">
								Revisions:
							</xsl:when>
							<xsl:otherwise>
								Packages:
							</xsl:otherwise>
						</xsl:choose>
					</div>
					<ul>
					<xsl:for-each select="//directories/directory">
						<xsl:param name="dirName" select="@name" />
						<li>
							<a href="{$dirName}">
								<xsl:value-of select="@name" />
							</a>
						</li>
					</xsl:for-each>
					</ul>
				</div>
				<xsl:call-template name="footer" />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>