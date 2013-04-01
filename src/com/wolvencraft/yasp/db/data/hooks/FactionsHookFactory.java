package com.wolvencraft.yasp.db.data.hooks;

public class FactionsHookFactory implements PluginHookFactory {
    
    private static FactionsHookFactory instance;
    
    public FactionsHookFactory() {
        
    }
    
    public static FactionsHookFactory getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub
        
    }
    
    public class FactionsHook implements PluginHook {

        @Override
        public void fetchData() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean pushData() {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
    
}
