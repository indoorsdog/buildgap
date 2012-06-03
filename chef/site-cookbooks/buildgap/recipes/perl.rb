#
# Cookbook Name:: buildgap-shell
# Recipe:: default
#
# Copyright 2012, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

perlbrew_root = node['perlbrew']['perlbrew_root']

template "/etc/profile.d/perlbrew.sh" do
  source "perlbrew.sh.erb"
  mode "0644"
  variables(
    :perlbrew_root => perlbrew_root
  )
end

bash "perlbrew_init" do
  user "vagrant"
  code <<-EOH
    #{perlbrew_root}/bin/perlbrew init
  EOH
end

perlbrew_cpanm 'install_mymeta_requires' do
  perlbrew 'perl-5.14.2'
  modules ['App::mymeta_requires']
end

bash "perlbrew_chown_files_dont_know_why" do
  user "root"
  code <<-EOH
    chown -R --quiet vagrant /home/vagrant/perlbrew
  EOH
end