net.croz.tomcat.atomikos.tomcat-atomikos-jta
============================================

atomikos jta object factory for tomcat

Usage
=====

Configure Atomikos as usual.

Add to tomcat's context.xml this line:

<Transaction factory="net.croz.tomcat.atomikos.AtomikosUnifiedTransactionManagerFactory">

