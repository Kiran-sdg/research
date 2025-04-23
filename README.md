java -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8888 -XX:MaxPermSize=512M -Xmx3024m -jar aem-sdk-p4502-author.jar -nofork -gui -port 4502 -r author


The method getDescriptionSpclItems() of type NcsecuListImpl must override or implement a supertype method

System.out.println("NcsecuListImpl: textSpclItems = " + textSpclItems);
System.out.println("NcsecuListImpl: descriptionSpclItems = " + descriptionSpclItems);



Caused by: org.apache.sling.models.factory.PostConstructException: Post-construct method has thrown an exception for model class org.ncsecu.aem.site.core.models.impl.NcsecuListImpl
	at org.apache.sling.models.impl.ModelAdapterFactory.createObject(ModelAdapterFactory.java:779) [org.apache.sling.models.impl:1.7.8]
	at org.apache.sling.models.impl.ModelAdapterFactory.internalCreateModel(ModelAdapterFactory.java:409) [org.apache.sling.models.impl:1.7.8]
	at org.apache.sling.models.impl.ModelAdapterFactory.createModel(ModelAdapterFactory.java:236) [org.apache.sling.models.impl:1.7.8]
	at org.apache.sling.scripting.sightly.impl.engine.extension.use.JavaUseProvider.loadObject(JavaUseProvider.java:183) [org.apache.sling.scripting.sightly:1.4.26.140]
	at org.apache.sling.scripting.sightly.impl.engine.extension.use.JavaUseProvider.provide(JavaUseProvider.java:130) [org.apache.sling.scripting.sightly:1.4.26.140]
	at org.apache.sling.scripting.sightly.impl.engine.extension.use.UseRuntimeExtension.call(UseRuntimeExtension.java:71) [org.apache.sling.scripting.sightly:1.4.26.140]
	... 429 common frames omitted
Caused by: java.lang.NullPointerException: null
