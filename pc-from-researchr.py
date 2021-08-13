#!/usr/bin/env python3

import sys
from html.parser import HTMLParser
import urllib.request
import os.path

# first argument is conference name
# second argument is four-digit year
# third argument is URL for main PC
# any remaining arguments are for external PCs
# we parse the URLs and output the PC members to the appropriate pc.txt file,
# creating directories if needed

if len(sys.argv) < 4:
    print("ERROR: must provide conference name, conference year, and PC URL as arguments")
    exit(1)

conf_name = sys.argv[1]
conf_year = sys.argv[2]
pc_url = sys.argv[3]
external_pc_urls = sys.argv[4:]

output_directory = "data/%s/%s"%(conf_name,conf_year)
output_file = "%s/%s%s-pc.txt"%(output_directory,conf_name.lower(),conf_year)

if os.path.exists(output_file):
    print("ERROR: output file %s already exists"%output_file)
    exit(1)

class MyHTMLParser(HTMLParser):

    def __init__(self):
        self.in_pc_member_element = False
        self.pc_members = []
        HTMLParser.__init__(self)
    
    def handle_starttag(self, tag, attrs):
        self.in_pc_member_element = tag == "h3" and ("class", "media-heading") in attrs

    def handle_endtag(self, tag):
        if self.in_pc_member_element and tag == "h3":
            self.in_pc_member_element = False

    def handle_data(self, data):
        if self.in_pc_member_element:
            self.pc_members.append(data.rstrip())

# collect the data.  return PC member names as a list
def parse_pc_data(url):
    response = urllib.request.urlopen(url)
    data = response.read()
    text = data.decode('utf-8')
    parser = MyHTMLParser()
    parser.feed(text)
    return parser.pc_members

pc_members = parse_pc_data(pc_url)
external_pc_members = []
for external_url in external_pc_urls:
    external_pc_members.extend(parse_pc_data(external_url))

text_result = ""
for pc_member in pc_members:
    text_result = text_result + pc_member + "\n"

for ext_member in external_pc_members:
    text_result = text_result + "E:" + ext_member + "\n"

if not os.path.exists(output_directory):
    os.makedirs(output_directory)

with open(output_file, "w") as o:
    print(text_result, file=o)

print("Wrote PC data to %s"%output_file)
print("Please check for errors and to add information on chairs")
