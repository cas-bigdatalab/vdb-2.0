package vdb.mydb.vtl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import vdb.mydb.VdbManager;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;

public class ExprParser {
    class SimpleNodeProxy implements Serializable {
        private SimpleNode _node;

        public SimpleNodeProxy(SimpleNode node) {
            super();
            _node = node;
        }

        public SimpleNode getNode() {
            return _node;
        }

        public void setNode(SimpleNode node) {
            _node = node;
        }
    }

    Context _vc;

    public ExprParser(Context vc) {
        _vc = vc;
    }

    public Object eval(String vtl) throws Exception {
        try {
            Cache cache = getCache();
            Element e = cache.get(vtl);
            RuntimeInstance ri = new RuntimeInstance();
            if (e == null) {
                String vtl2 = "#set ($__var__=" + vtl + ")";
                SimpleNode node = ri.parse(new StringReader(vtl2), vtl);
                e = new Element(vtl, new SimpleNodeProxy(node));
                cache.put(e);
            }

            StringWriter sw = new StringWriter();
            SimpleNode node = ((SimpleNodeProxy) e.getValue()).getNode();
            ri.render(_vc, sw, ExprParser.class.getName(), node);

            return _vc.get("__var__");
        } catch (Throwable t) {
            //t.printStackTrace();
            //Logger.getLogger(this.getClass()).debug("vtl: " + vtl);
            return vtl;
        }
    }

    private Cache getCache() throws CacheException {
        return VdbManager.getInstance().getCache(this.getClass().getName());
    }

    public String evalString(String vtl) throws Exception {
        try {
            StringWriter sw = new StringWriter();
            new VelocityEngine().evaluate(_vc, sw, "", vtl);
            return sw.toString();
        } catch (Throwable t) {
            t.printStackTrace();
            return vtl;
        }
    }
}
