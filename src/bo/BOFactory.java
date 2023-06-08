package bo;

import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.ItemBOImpl;
import bo.custom.impl.PurchaseOrderBOImpl;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 3:07 PM
 **/

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory(){
    }

    public static BOFactory getBoFactory(){
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes{
        CUSTOMER, ITEM, PURCHASE_ORDER
    }

    public SuperBO getBO(BOTypes types){
        switch (types){
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case PURCHASE_ORDER:
                return new PurchaseOrderBOImpl();
            default:
                return null;
        }
    }
}
