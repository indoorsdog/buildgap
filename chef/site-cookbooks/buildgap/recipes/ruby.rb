#
# Cookbook Name:: buildgap-shell
# Recipe:: default
#
# Copyright 2012, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

include_recipe "rbenv::ruby_build"

rbenv_ruby "1.9.3-p194"

rbenv_gem "bundler" do
  version "1.1.3"
  ruby_version "1.9.3-p194"
end

template "/vagrant/.rbenv-version" do
  source ".rbenv-version.erb"
  mode "0644"
  variables(
    :ruby_version => "1.9.3-p194"
  )
end

package "libxslt-dev" do
  action :install
end

package "libxml2-dev" do
  action :install
end