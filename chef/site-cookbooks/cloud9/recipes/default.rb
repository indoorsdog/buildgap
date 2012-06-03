#
# Cookbook Name:: ide
# Recipe:: default
#
# Copyright 2012, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

# This should be just a transitive dependency to me
include_recipe "build-essential"

include_recipe "nodejs"
include_recipe "git"


# https://github.com/ajaxorg/cloud9/issues/1564
package "libxml2-dev" do
  action :install
end

# https://github.com/ajaxorg/cloud9#installation-and-usage
bash "install_cloud9" do
  user "root"
    cwd "/home/vagrant"
    code <<-EOH
    sudo npm install -g sm
    git clone https://github.com/ajaxorg/cloud9.git cloud9
    cd cloud9
    sudo sm install
    EOH
end