
to install knife, install chef
to install chef
  
  gem install chef


install cookbook from this folder, with example version 1.0.0 of "build-essential"



./cookbooks must be a git repository
http://help.opscode.com/discussions/problems/1196-the-cookbook-repo-usersuserchefcookbooks-is-not-a-git-repository
so run "git init" in it first

and, well, that blows up http://tickets.opscode.com/browse/CHEF-2512
and as of today 2012-04-18, fix is only on HEAD, chef is at 0.10.8 so reinstall chef using http://wiki.opscode.com/display/chef/Installing+Chef+from+HEAD

i don't even know what is going on, install gecode from source, when on ubuntu? 
http://wiki.opscode.com/display/chef/Upgrading+Chef+0.9.x+to+Chef+0.10.x#UpgradingChef0.9.xtoChef0.10.x-BuildandInstallfromSource


and then you can't just put it anywhere, you gotta clone chef-repo
git clone git://github.com/opscode/chef-repo.git
 and stick it in its /cookbooks
http://tickets.opscode.com/browse/CHEF-1446

in vm, cd /vagrant
haha, can now do this 
knife cookbook site install rvm 0.0.4 --cookbook-path /vagrant/chef-repo/cookbooks



to create a cookbook


knife cookbook create buildgap-shell --cookbook-path /vagrant/chef-repo/cookbooks



http://jetpackweb.com/blog/2011/06/12/manage-your-third-party-chef-cookbooks-with-knife-github-cookbooks/