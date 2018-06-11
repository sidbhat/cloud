
dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {

	/***
		Development instance uses oneapp_dev schema
	***/
    development {
       dataSource {
            driverClassName = "com.mysql.jdbc.Driver"
            username = "root"
            password = "root"
            dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
            dbCreate = "update"
            pooled=true
     		properties { 
     			maxActive=10
     			initialSize=5
     		    maxIdle = 25
        		minIdle = 5
        		initialSize = 5
        		minEvictableIdleTimeMillis = 60000
        		timeBetweenEvictionRunsMillis = 60000
        		maxWait = 10000
        		removeAbandonedTimeout=60
        		removeAbandoned=true
        		validationQuery = "SELECT 1"
     		}
            url = "jdbc:mysql://localhost:3306/oneapp_dev?useUnicode=true&characterEncoding=utf8&autoReconnect=true"
      
       } 
       
    }
    
}
