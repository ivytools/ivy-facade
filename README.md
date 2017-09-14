# ivy-facade
A clone of Ivy Facade project at Google Code

# About

## Introduction

Ivy Facade is a project aimed at visualizing an Ivy Repository. After working through several iterations of creating an enterprise repository I had found that managing and finding libraries was becoming much more difficult as my library grew, so out of need I decided to create this project.

The biggest reason I have spent many hours creating a decent repository is because the Open source landscape is in a bit of disarray. If you doubt that, just visit java.net, or try to find out where xmlsec.jar really came from, and how it is actually licensed.

Just as important as any of that, is how long it takes to create a project from scratch, how do I know what is the latest, where to get it, how it's licensed, etc, etc...

So from this chaos I hope this tool can bring a little order, at least the facade of order (pun intended). Enjoy, and please provide feedback.

## Requirements

Java 1.6+

As of version 1.0.13, a more modern browser is required. I have updated the site to use inline images and this is only supported by:

 * Google Chrome
 * Firefox 3+
 * IE 8+
 * Safari 3+ and others.

The primary reason for doing this was to reduce the pollution to the Ivy directory structure, the secondary is to move the site capabilities to also serve to mobile devices.

# Building Ivy-Facade

## Prerequisites

1. Ant 1.8 or later should be installed and on your path:
2. The Ivy Libraries should be added to your \/lib directory
3. You will need Internet access
4. Download the most recent source code as a jar, or from Github.

## Files of interest

* build.properties - this file contains the common properties of the build, including the build number.
* build.xml - the build control file for ant.
* ivy.xml - this describes and describes the dependencies of ivy facade.
* ivysettings.xml - this file is updated from ivy.procippus.com. This is a very clean ivy repository. I have gone to great lengths to make it useful to the frameworks I most often use.

## Details

Once you have the source code and Ant setup, simply use your shell or command prompt to run the build with following command:

    ant -f build.xml dist

You should then see a similar output:

    Buildfile: /IvyFacade/IvyFacade/build.xml
         [echo] /usr/share/java/ant-1.8.2
          [get] Getting: http://ivy.procippus.com/ivysettings.xml
          [get] To: /IvyFacade/IvyFacade/ivysettings.xml
    clean:
       [delete] Deleting directory /IvyFacade/IvyFacade/lib
       [delete] Deleting directory /IvyFacade/IvyFacade/build
        [mkdir] Created dir: /IvyFacade/IvyFacade/lib
        [mkdir] Created dir: /IvyFacade/IvyFacade/build
        [mkdir] Created dir: /IvyFacade/IvyFacade/build/bin
        [mkdir] Created dir: /IvyFacade/IvyFacade/build/dist
    resolve:
    [ivy:configure] :: Ivy 2.2.0 - 20100923230623 :: http://ant.apache.org/ivy/ ::
    [ivy:configure] :: loading settings :: url = jar:file:/usr/share/java/ant-1.8.2/lib/ivy-2.2.0.jar!/org/apache/ivy/core/settings/ivysettings.xml
    [ivy:resolve] :: resolving dependencies :: com.procippus#IvyFacade;working@Prometheus.local
    [ivy:resolve]   confs: [default]
    [ivy:resolve]   found org.eclipse#jdt.internal.jarinjarloader;1.3.0 in default
    [ivy:resolve]   found nu#xom;1.2.6 in default
    [ivy:resolve]   found org.apache#ant;1.8.2 in default
    ...
    BUILD SUCCESSFUL
    Total time: 9 seconds

# Usage

## Assumptions

In order to make the facade usable, there is one assumption, which is currently being worked on to
eliminate, you need to download the source code, and place the assets directory
(/resources/assets) in the root of your sever hosting Ivy. 

Example:

    Apache Root Directory: /usr/local/apache/htdocs

    Copy the assets directory to: /usr/local/apache/htdocs/assets

* This directory can be changed in the "common.xsl" file which is created after the initial run.

## Stand-alone application

    java -jar IvyFacade-<version>.jar /usr/local/apache/htdocs/ivyrepo

## Ant task


    <project name="ivyFacade" basedir="." default="createIvyFacade">
        <taskdef name="ivyFacade" classname="com.procippus.ivy.IvyFacadeTask" />
        <target name="createIvyFacade">
            <ivyFacade ivyRoot="/usr/local/apache/htdocs/ivy-local" />
        </target>
    </project>

## Useful properties

    ## XSL Files for rendering content
    ## These are generated from the templates upon first run.

    default.xsl.ivy=support/ivy.xsl
    default.xsl.dir=support/directory.xsl
    default.xsl.home=support/home.xsl
    default.xsl.common=support/common.xsl

    ## Directories to ignore when crawling
    files.ignore.directory=assets

    ## Files to ignore when parsing directories
    files.ignore=index.html,ivysettings,ivydata,.DS

    ## File patterns to read as ivy.xml files
    files.ivy.patterns=(^ivy.xml$)|(^ivy-(.*)\\.xml$)

    ## Controls the size of the graphics (Note, the size below was tested against
    ## some very large dependency graphs

    graphics.width=750
    graphics.height=550

    # Controls the type of graphics created
    graphics.type=png
    graphics.extension=.png

    # Absolute path from the web root to read the assets directory from (CSS and JavaScript used by site)
    assets.root.absolute.url=/

    # Absolute path to write the assets directory to:
    assets.root.write=
