package "openssl" do
  action :install
end

package "dtach" do
  action :install
end

bash "install_ide" do
  user "vagrant"
  code <<-EOH
    sudo git clone git://github.com/pizzapanther/Neutron-IDE.git
    cd Neutron-IDE
    sudo pip install -r requirements.txt
    cd neutron
    ./manage.py syncdb #create a user account when asked
    ./manage.py migrate
    ./neutronide.py -l -f start
  EOH
end

# nah, go with cloud9, and use hints here https://github.com/ajaxorg/cloud9/issues/1564