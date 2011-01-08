package io.redis.rjc;

/**
 * @author Evgeny Dolgov
 */
public class RedisTemplate {

    private SessionFactory factory;

    public RedisTemplate() {
    }

    public RedisTemplate(SessionFactory factory) {
        this.factory = factory;
    }

    public SessionFactory getFactory() {
        return factory;
    }

    public void setFactory(SessionFactory factory) {
        this.factory = factory;
    }

    public<T> T execute(RedisCallback<T> action) {
        Session session = factory.create();
        try {
            return action.doIt(session);
        } finally {
            session.close();
        }
    }

}
