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
	<xsl:output method="xml" version="1.0" encoding="UTF-8" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" indent="yes"/>
	<xsl:param name="organisation" />
	<xsl:param name="imageExtension" />
	<xsl:import href="common.xsl" />

	<xsl:template match="/">
		<xsl:param name="info" select="//info" />

		<xsl:param name="org" select="$info/@organisation" />
		<xsl:param name="module" select="$info/@module" />
		<xsl:param name="revision" select="$info/@revision" />
		<xsl:param name="status" select="$info/@status" />
		<xsl:param name="publication" select="$info/@publication" />
 		
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
			<head>
				<title>Ivy Facade - <xsl:value-of select="$org" />  / <xsl:value-of select="$module" />  / <xsl:value-of select="$revision" />  </title>
				<xsl:call-template name="header" />
				<script type="text/javascript">
					$(function(){
						$('#tabs').tabs();
					});
 				</script>
			</head>
			<body>
				<xsl:call-template name="banner" />
				<div class="bread-crumbs">
					<xsl:choose>
						<xsl:when test="//@missingDependencies = 'true'">
							<div class="error">
								<a href="../../../">Home</a> / <a href="../../"><xsl:value-of select="$org" /></a> / 
								<a href="../"><xsl:value-of select="$module" /></a> / <xsl:value-of select="$revision" />
							</div>
						</xsl:when>
						<xsl:otherwise>
							<a href="../../../">Home</a> / <a href="../../"><xsl:value-of select="$org" /></a> / 
							<a href="../"><xsl:value-of select="$module" /></a> / <xsl:value-of select="$revision" />
						</xsl:otherwise>
					</xsl:choose>
					
				</div>
				
				<div class="content">
					<xsl:param name="descr" select="//info/description" />
					<xsl:if test="$descr">
						<div class="description">
							<div class="subtitle">Description:</div>
							<div>
								<xsl:value-of select="$descr" disable-output-escaping="yes" />
							</div>
						</div>
					</xsl:if>
		
					<div id="tabs">
	                  <ul>
	                      <li><a href="#libraries">Jar Files (<xsl:value-of select="count(//publications/artifact[not(@type)] | //publications/artifact[@type='jar']) " />)</a></li>
	                      <li><a href="#sources">Source Code (<xsl:value-of select="count(//publications/artifact[@type='src'])" />)</a></li>
	                      <li><a href="#licenses">License Files (<xsl:value-of select="count(//publications/artifact[@type='license'])" />)</a></li>
	                      <li><a href="#dependencies">Dependencies (<xsl:value-of select="count(//dependencies/dependency)" />)</a></li>
	                      <li><a href="#dependents">Dependents (<xsl:value-of select="count(//dependents/dependency)" />)</a></li>
	                      <li><a href="#graph">Graph</a></li>
	                  </ul>
						<div id="libraries">
						<ul>
							<xsl:for-each select="//publications/artifact">
								<xsl:if test="not(@type) or @type = 'jar'">
								<xsl:param name="libLink"><xsl:value-of select="@name" />.jar</xsl:param>
								<li>
									<a href="{$libLink}">
									<xsl:value-of select="@name" />.jar
									</a>
								</li>
								</xsl:if>
							</xsl:for-each>
						</ul>
						</div>
		
						<div id="sources">
						<xsl:choose>
							<xsl:when test="count(//publications/artifact[@type='src']) &gt; 0">
								<ul>
									<xsl:for-each select="//publications/artifact">
										<xsl:if test="@type = 'src'">
										<xsl:param name="srcLink"><xsl:value-of select="@name" />.<xsl:value-of select="@ext" /></xsl:param>
										<li>
											<a href="{$srcLink}">
											<xsl:value-of select="@name" />.<xsl:value-of select="@ext" />
											</a>
										</li>
										</xsl:if>
									</xsl:for-each>
								</ul>
							</xsl:when>
							<xsl:otherwise>
								No source files listed.
							</xsl:otherwise>
						</xsl:choose>
						
						</div>
					
						<div id="licenses">
						<xsl:choose>
							<xsl:when test="count(//publications/artifact[@type='license']) &gt; 0">
								<ul>
									<xsl:for-each select="//publications/artifact">
										<xsl:if test="@type = 'license'">
										<xsl:param name="licLink"><xsl:value-of select="@name" />.<xsl:value-of select="@ext" /></xsl:param>
										<li>
											<a href="{$licLink}">
											<xsl:value-of select="@name" />.<xsl:value-of select="@ext" />
											</a>
										</li>
										</xsl:if>
									</xsl:for-each>
								</ul>
							</xsl:when>
							<xsl:otherwise>
								No license files listed, please review source code for applicable license(s).
							</xsl:otherwise>
						</xsl:choose>
						</div>
						
						<div id="dependencies">
							<xsl:choose>
							<xsl:when test="not(//dependencies/dependency)">
								No dependencies listed.
							</xsl:when>
							<xsl:otherwise>
							<ul>
								<xsl:for-each select="//dependencies/dependency">
									<li>
										<xsl:param name="depLink">../../../<xsl:value-of select="@org" />/<xsl:value-of select="@name" />/<xsl:value-of select="@rev" />/</xsl:param>
										<xsl:choose>
											<xsl:when test="@missing">
												<div class="error">
													<xsl:value-of select="@org" /> - <xsl:value-of select="@name" /> - <xsl:value-of select="@rev" />
												</div>
											</xsl:when>
											<xsl:otherwise>
												<div>
													<a href="{$depLink}"><xsl:value-of select="@org" /> - <xsl:value-of select="@name" /> - <xsl:value-of select="@rev" /></a>
												</div>
											</xsl:otherwise>
										</xsl:choose>
									</li>
								</xsl:for-each>
								</ul>
							</xsl:otherwise>
							</xsl:choose>
							
						</div>
						
						<div id="dependents">
							<xsl:choose>
							<xsl:when test="not(//dependents/dependency)">
								No dependents listed.
							</xsl:when>
							<xsl:otherwise>
							<ul>
								<xsl:for-each select="//dependents/dependency">
									<li>
										<xsl:param name="dependentLink">../../../<xsl:value-of select="@org" />/<xsl:value-of select="@name" />/<xsl:value-of select="@rev" />/</xsl:param>
										<a href="{$dependentLink}"><xsl:value-of select="@org" /> - <xsl:value-of select="@name" /> - <xsl:value-of select="@rev" /></a>
									</li>
								</xsl:for-each>
								</ul>
							</xsl:otherwise>
							</xsl:choose>
						</div>
						
						<div id="graph">
							<div style="text-align: center;">
								<img src="graph{$imageExtension}" style="border: 1px solid #999;" />
							</div>
						</div>
					</div>
					
					
					
					<div style="margin-top: 10px;">Ivy Usage:</div>
					<div id="usage" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
						&lt;dependency org=&quot;<xsl:value-of select="$org"/>&quot; name=&quot;<xsl:value-of select="$module" />&quot; rev=&quot;<xsl:value-of select="$revision" />&quot; /&gt;
					</div>
				</div>
				<xsl:call-template name="footer" />
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>