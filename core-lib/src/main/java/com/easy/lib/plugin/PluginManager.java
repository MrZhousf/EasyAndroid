package com.easy.lib.plugin;

import android.content.Context;

import com.easy.lib.util.Util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {

    private final String TAG = getClass().getSimpleName();

    private static PluginManager singleton;

    /**
     * 基础插件：随着Application启动而初始化
     */
    private Map<Class, IPlugin> pluginMap;
    private Map<Class, Boolean> pluginStateMap;
    private boolean  pluginLoaded;

    /**
     * 延迟插件：随着第一个Activity启动而初始化
     */
    private Map<Class, IPlugin> delayPluginMap;
    private Map<Class, Boolean> delayPluginStateMap;
    private boolean delayPluginLoaded;

    public static PluginManager get() {
        if (null == singleton) {
            synchronized (PluginManager.class) {
                if (null == singleton) {
                    singleton = new PluginManager();
                }
            }
        }
        return singleton;
    }

    private PluginManager() {
        pluginMap = new ConcurrentHashMap<>();
        pluginStateMap = new ConcurrentHashMap<>();
        delayPluginMap = new ConcurrentHashMap<>();
        delayPluginStateMap = new ConcurrentHashMap<>();
    }

    /**
     * 注册基础插件：随着Application启动而初始化
     * @param plugin 插件
     */
    public PluginManager registerPlugin(IPlugin plugin){
        pluginMap.put(plugin.getClass(),plugin);
        pluginStateMap.put(plugin.getClass(),false);
        return this;
    }

    /**
     * 注册延迟插件：随着第一个Activity启动而初始化
     * @param plugin 插件
     * @param delay  是否延迟加载
     */
    public PluginManager registerPlugin(IPlugin plugin, boolean delay){
        if(delay){
            delayPluginMap.put(plugin.getClass(),plugin);
            delayPluginStateMap.put(plugin.getClass(),false);
        }else {
            registerPlugin(plugin);
        }
        return this;
    }

    public void init(Context context, boolean delay){
        if(delay){
            initPlugin(context,delayPluginMap,delayPluginStateMap,delay);
        }else{
            initPlugin(context,pluginMap,pluginStateMap,delay);
        }
    }

    private synchronized void initPlugin(Context context, Map<Class, IPlugin> plugins, Map<Class, Boolean> pluginState, boolean delay){
        if(delay){
            if(delayPluginLoaded){
                return ;
            }else{
                delayPluginLoaded = true;
            }
        }else{
            if(pluginLoaded){
                return ;
            }else{
                pluginLoaded = true;
            }
        }
        boolean succeed;
        int successNum = 0;
        for(Map.Entry<Class, IPlugin> entry : plugins.entrySet()){
            //判断是否已经初始化
            if(!pluginState.get(entry.getValue().getClass())){
                succeed = entry.getValue().initPlugin(context);
                pluginState.put(entry.getValue().getClass(),succeed);
                if(succeed){
                    successNum ++;
                }
            }else{
                successNum ++;
            }
        }
        int totalNum = pluginState.size();
        String log = "启动"+(delay?"延迟":"基础")+"插件共："+totalNum+"个，成功："
                +successNum+"个，失败："+(totalNum-successNum)+"个";
        if(totalNum != successNum){
            Util.Log.e(TAG,log);
        }else{
            Util.Log.d(TAG,log);
        }
    }

    /**
     * 获取插件
     * @param clazz 插件模型
     */
    @SuppressWarnings(value = "unchecked")
    public <T> T getPlugin(Class clazz){
        T plugin = null;
        if(clazz!=null){
            IPlugin corePlugin = pluginMap.get(clazz);
            if(corePlugin == null){
                corePlugin = delayPluginMap.get(clazz);
            }
            if(corePlugin != null){
                plugin = (T) corePlugin;
            }
        }
        return plugin;
    }

}
