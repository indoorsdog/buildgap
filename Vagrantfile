# -*- mode: ruby -*-
# vi: set ft=ruby :

librarian_name = 'librarian'
librarian_version = '0.0.23'
begin
  Gem::Specification.find_by_name(librarian_name, librarian_version)
rescue Gem::LoadError
  begin
    require 'vagrant/environment'
    env = Vagrant::Environment.new
    env.cli('gem', 'install', librarian_name, '--version', librarian_version)
  rescue SystemExit
  end
end

Vagrant::Config.run do |config|

  config.vm.network :hostonly, '10.151.151.151', :netmask => '255.255.0.0'
  config.vm.host_name = 'buildgap'

  config.vm.box = "buildgap"
  config.vm.box_url = "http://files.vagrantup.com/precise64.box"

  config.vm.provision :chef_solo do |chef|

    chef.log_level = :debug

    require 'librarian/chef/cli'

    puts Dir.pwd
    Dir.chdir('chef')
    cli = Librarian::Chef::Cli.new
    cli.install

    chef.cookbooks_path = ['chef/cookbooks', 'chef/site-cookbooks']

    chef.add_recipe "buildgap::git"

    chef.add_recipe "build-essential"

    # http://grahamwideman.wikispaces.com/Python-+import+statement
    # pip install -r requirements.txt
    chef.add_recipe "python::source"
    chef.add_recipe "python::pip"
    chef.add_recipe "python::virtualenv"
    chef.add_recipe "buildgap::python"

#need PIL http://www.pythonware.com/products/pil/
#member, sudo apt-get install python-dev
    #chef.add_recipe "buildgap::ide"

    # package.json
    # npm install -d
    # http://tnovelli.net/blog/blog.2011-08-27.node-npm-user-install.html
    chef.add_recipe "nodejs"

    # todo version info for ruby_build in attributes
    chef.add_recipe "rbenv::system_install"
    chef.add_recipe "rbenv::ohai_plugin"
    chef.add_recipe "buildgap::ruby"

    chef.add_recipe "java"

    chef.add_recipe "gradle::tarball"

    # http://www.dagolden.com/index.php/1528/five-ways-to-install-modules-prereqs-by-hand/
    chef.add_recipe "perlbrew"
    chef.add_recipe "buildgap::perl"

    chef.add_recipe "cloud9"

    chef.json = {
    	"python" => {
        "version" => "2.7.3",
        "distribute_install_py_version" => "2.7"
      },
      "git" => {
        "version" => "1:1.7.9.5-1"
      },
      "nodejs" => {
        "version" => "0.6.18"
      },
      "java" => {
        "install_flavor" => "openjdk",
        "jdk_version" => "7"
      },
      "gradle" => {
        "version" => "1.0-rc-3",
        "tarball" => {
          "url" => "http://downloads.gradle.org/distributions/gradle-1.0-rc-3-bin.zip"
        }
      },
      "perlbrew" => {
        "perlbrew_root" => "/home/vagrant/perlbrew",
        "perls" => ["perl-5.14.2"]
      }
    }
  end

end