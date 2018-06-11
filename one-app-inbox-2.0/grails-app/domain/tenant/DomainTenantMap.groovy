 
package tenant

import java.io.Serializable;

/**
 * Maps domain name to tenantId
 */
class DomainTenantMap implements Serializable{
    String domainName
    Integer mappedTenantId
    String name
    static constraints = {}


}
