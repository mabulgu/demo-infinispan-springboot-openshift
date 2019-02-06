This is a simple spring boot application used for a demo of Infinispan / Red Hat Data Grid  usage with MySQL on Openshift / Kubernetes.

First you have to have a running openshift instance. You can either use minishift (https://github.com/minishift/minishift) or "oc cluster up" for this.

Here we will use minishift and since we use minishift we will have a user called "developer" who is a basic user. You can use whatever user you want, or just can use the cluster admin for your trials. I tried to make as clear as possible with the two users: system:admin as cluster admin and developer as basic user.

Once you have a running openshift let's install JDG on OpenShift.

We can use the basic template of JDG for this.

Switch to the system:admin user to have save the jdg template here:  

~~~shell
$ oc login -u system:admin
~~~

switch to openshift project

~~~shell
$ oc project openshift
~~~

and create the template here

~~~shell
$ oc create -f https://raw.githubusercontent.com/jboss-container-images/jboss-datagrid-7-openshift-image/1.3/templates/datagrid72-basic.json
~~~

Check the template if it is created successfully:

~~~shell
$ oc get templates
~~~

Grep the current images with this command anf see if there is datagrid image exists:

~~~shell
$ oc get images | grep datagrid
~~~

If you use a local openshift you won't have this image or you may have the different version of the image so you should import it. This will both create the image and image stream in openshift namespace:

~~~shell
$ oc import-image jboss-datagrid72-openshift:1.3 --from=registry.access.redhat.com/jboss-datagrid-7/datagrid72-openshift:1.3 --confirm -n openshift
~~~

Check the image existance again:

~~~shell
$ oc get images | grep datagrid
~~~

Check the image streams of openshift namespace:

~~~shell
$ oc get is -n openshift
~~~

Before switching to the developer user to create the project and application we have to give the rights for the developer user allowing him/her to view and pull the images:

~~~shell
$ oc policy add-role-to-user registry-viewer developer
~~~

Now login with the developer user and create a new project like "jdg-project":

~~~shell
$ oc login -u developer
~~~

~~~shell
$ oc new-project jdg-project
~~~

Create a new app in the jdg-project with the datagrid72-basic template

~~~shell
$ oc new-app datagrid72-basic -n jdg-project
~~~


