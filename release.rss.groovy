#!/usr/bin/env groovy
/*
 generate the plugin/core release history RSS

 Usage: release-rss.groovy <URL of release-hisotory.json>
*/
@Grab(group="net.sf.json-lib",module="json-lib",version="2.4",classifier="jdk15")
import net.sf.json.groovy.JsonSlurper
import groovy.xml.MarkupBuilder
import java.text.*;

def xsd(dt) {
  s=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(dt);
  return s[0..-3]+':'+s[-2..-1]
}
def df=new SimpleDateFormat("MMM dd, yyyy");

def json = new JsonSlurper().parse(new URL(args[0]));

def xml = new MarkupBuilder(new OutputStreamWriter(System.out))
xml.feed(xmlns:"http://www.w3.org/2005/Atom") {
  title("Jenkins plugin releases")
  link(href:"http://jenkins-ci.org/")
  link(href:"http://kohsuke.org/test.atom",rel:"self",type:"application/atom+xml")
  updated(xsd(new Date()))
  author { name("Jenkins History Bot") }
  id("urn:63067410335c11e0bc8e0800200c9a66:feed")

  json.releaseHistory.reverse().subList(0,30).each { i ->
    i.releases.each { r ->
      entry {
        title("${r.title} ${r.version}")
        link(href:r.wiki)
        id("urn:63067410335c11e0bc8e0800200c9a66:${r.gav}")
        pubished(xsd(new Date(r.timestamp)))
        updated(xsd(new Date(r.timestamp)))
        summary("${r.title} ${r.version}")
      }
    }
  }
}
