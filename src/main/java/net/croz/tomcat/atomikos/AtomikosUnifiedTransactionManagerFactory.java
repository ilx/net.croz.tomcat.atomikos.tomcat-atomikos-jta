package net.croz.tomcat.atomikos;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import com.atomikos.icatch.jta.J2eeTransactionManager;
import com.atomikos.icatch.jta.TransactionManagerImp;
import com.atomikos.icatch.jta.UserTransactionManager;

/**
 * Atomikos transactionmanager implementation that can be used inside tomcat's context.xml instead of UserTransactionFactory. 
 * 
 * Do not use 
 * @author iloncar
 *
 */
public class AtomikosUnifiedTransactionManagerFactory implements ObjectFactory {
    
    private static final String JTA_JAVAX_TRANSACTION_USER_TRANSACTION = "javax.transaction.UserTransaction";
    private static final String JTA_J2EE_TRANSACTION_MANAGER = "com.atomikos.icatch.jta.J2eeTransactionManager";
    private static final String JTA_TRANSACTION_MANAGER_IMP = "com.atomikos.icatch.jta.TransactionManagerImp";

    public Object getObjectInstance( Object p_obj, Name p_name, Context p_context, Hashtable<?, ?> p_envmt ) throws Exception {
        Object ret = null;
        if ( p_obj == null || !(p_obj instanceof Reference) )
            return null;
        Reference ref = (Reference) p_obj;

        System.out.println("TransactionManager initialization!");
        System.out.println("Ref:" + ref);
        
        if ( ref.getClassName ().equals(JTA_TRANSACTION_MANAGER_IMP ) )
            ret = TransactionManagerImp.getTransactionManager ();
        else {
            if ( ref.getClassName ().equals ( JTA_J2EE_TRANSACTION_MANAGER ) ) {
                ret = new J2eeTransactionManager ();                
            }
            else if ( ref.getClassName().equals (JTA_JAVAX_TRANSACTION_USER_TRANSACTION )) {
                System.out.println("TransactionManager initialization using TransactionManagerImp!");
                UserTransactionManager userTransactionManager = new UserTransactionManager();
                userTransactionManager.init();
                ret = userTransactionManager;
                System.out.println("Ret:" + ret.toString());                
            }
            else {
                StringBuilder sb = new StringBuilder("Unable to instantiate AtomikosUnifiedTransactionManagerImpl. ClassName should be one of: ");
                sb.append( JTA_J2EE_TRANSACTION_MANAGER ).append( "," );
                sb.append( JTA_TRANSACTION_MANAGER_IMP).append( "," );
                sb.append( JTA_JAVAX_TRANSACTION_USER_TRANSACTION );
                
                throw new IllegalStateException(sb.toString());
            }
        }

        return ret;
    }

}
