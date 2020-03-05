package com.pluralsight.rxjava2.nitrite;

import org.dizitart.no2.Nitrite;

public class NO2 {

    public static <T> T execute( Nitrite database, NitriteUnitOfWorkWithResult<T> unitOfWork) {
        try {
            return unitOfWork.apply(database);
        }
        catch(Exception e ) {
            throw new RuntimeException(e);
        }
        finally {
            database.commit();
        }
    }

    public static void execute( Nitrite database, NitriteUnitOfWork unitOfWork) {
        try {
            unitOfWork.apply(database);
        }
        catch(Exception e ) {
            throw new RuntimeException(e);
        }
        finally {
            database.commit();
        }
    }
}
