package com.boot.config.redis;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

/**  
 *
 *项目名称：WeddingBOS
 *
 *描述：
 *
 *创建人：Mr.chang
 *
 *创建时间：2016年8月19日 上午10:48:04 
 *   
 * Copyright @ 2016 by Mr.chang  
 *   
 */
@Component
public class CacheRedisUtil {
	
	private static Logger log = LoggerFactory.getLogger(CacheRedisUtil.class);
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	/**
	 * 向SET中添加一个成员，为一个Key添加一个值。如果这个值已经在这个Key中，则返回FALSE
	 * 
	 * @param key
	 * @param values
	 * @param expireSeconds
	 */
	@SuppressWarnings("unchecked")
	public List<byte[]> lRange(final String key, final int start, final int end) {
		return (List<byte[]>) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public List<byte[]> doInRedis(RedisConnection connection)
							throws DataAccessException {
						List<byte[]> list = null;
						try {
							list = connection.lRange(key.getBytes("utf-8"), 0, -1);
						} catch (UnsupportedEncodingException e) {
							log.error(e.getMessage(), e);
						}
						return list;
					}
				});
	}
	
	/**
	 * 查询指定范围内元素的列表
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<byte[]> lList(final String key, final int start, final int end){
		return (List<byte[]>) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public List<byte[]> doInRedis(RedisConnection connection)
							throws DataAccessException {
						List<byte[]> list = null;
						try {
							list = connection.lRange(key.getBytes("utf-8"), start, end);
						} catch (UnsupportedEncodingException e) {
							log.error(e.getMessage(), e);
						}
						return list;
					}
				});
	}

	public long lLen(final String key) {
		long cnt = 0;
		try {
			lLen(key.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}

		return cnt;
	}
	
	/**
	 * @移除列表中的与参数相等的元素
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 * @author zhang
	 * @since 2016年3月30日
	 */
	@SuppressWarnings("unchecked")
	public long LRem(final String key,final int count,final String value){
		return (long)redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				try {
					result=connection.lRem(key.getBytes("utf-8"), count, value.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return result;
			}
		});
	}

	/**
	 * 通过索引号获得list对应的值
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String lIndex(final String key, final long index) {
		return (String) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] result = null;
				try {
					result = connection.lIndex(key.getBytes("utf-8"), index);
				} catch (UnsupportedEncodingException e1) {
					log.error(e1.getMessage(), e1);
				}
				try {
					return new String(result, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public long lLen(final byte[] key) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.lLen(key);
			}
		});
	}

	/**
	 * 判断是否存在某key
	 * 
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public Boolean exists(final String key) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				Boolean flag = null;
				flag=connection.exists(key.getBytes());
				return flag;
			}
		});
	}

	/**
	 * 取得key对应的list
	 * 
	 * @param key
	 * @return
	 */
	public List<byte[]> lRange(final String key) {
		return lRange(key, 0, -1);
	}

	/**
	 * 将一个值 value 插入到列表 key 的表尾(最右边)。
	 * 
	 * @param key
	 * @param value
	 * @param expireSeconds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long rPush(final String key, final String value,
			final int expireSeconds) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				long cnt = 0;
				try {
					connection.rPush(key.getBytes("utf-8"),
							value.getBytes("utf-8")).longValue();
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return cnt;
			}
		});
	}

	/**
	 * 将值value插入到列表key的表头
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long lPush(final String key, final String value) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				long cnt = 0;
				try {
					connection.lPush(key.getBytes("utf-8"),
							value.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return cnt;
			}
		});
	}

	/**
	 * 保留列表指定范围内的元素
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean lTrim(final String key,final int begin,final int end) {
		return (boolean) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					connection.lTrim(key.getBytes("utf-8"), begin, end);
					return true;
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return false;
			}
		});
	}
	/**
	 * 列表左侧删除一个元素
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public byte[] lPop(final String key) {
		return (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public byte[] doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] b = null;
				try {
					b = connection.lPop(key.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return b;
			}
		});
	}

	/**
	 * 向SET中添加一个成员，为一个Key添加一个值。如果这个值已经在这个Key中，则返回FALSE
	 * 
	 * @param key
	 * @param values
	 * @param expireSeconds
	 */
	@SuppressWarnings("unchecked")
	public void sadd(final byte[] key, final byte[] value,
			final int expireSeconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.sAdd(key, value);
				if (expireSeconds > 0) {
					connection.expire(key, expireSeconds);
				}
				return null;
			}
		});
	}

	/**
	 * 
	 * 插入时取得某张表的自增id值
	 * 
	 * @param key
	 *            示例:db.{tableName}.id
	 * @return 大于0时表示正常 -1 表示key必须填写
	 */
	public long incr(final String key) {
		return incr(0, key);
	}

	/**
	 * 
	 * 插入时取得某张表的自增id值
	 * 
	 * @param dbindex
	 *            数据库下标
	 * @param key
	 *            示例:db.{tableName}.id
	 * @return 大于0时表示正常 -1 表示key必须填写
	 */
	@SuppressWarnings("unchecked")
	public long incr(final int dbIndex, final String key) {
		if (StringUtils.isEmpty(key)) {
			return -1l;
		}
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				long id = 0;
				try {
					id = connection.incr(key.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return id;
			}
		});
	}

	/**
	 * 从SET中删除一个成员
	 * 
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void srem(final byte[] key, final byte[] value) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.sRem(key, value);
				return null;
			}
		});
	}

	/**
	 * 返回SET任一成员
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public byte[] srandmember(final byte[] key) {
		return (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] result = connection.sRandMember(key);
				return result;
			}
		});
	}

	/**
	 * 判断给定值是否为SET成员
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean sexist(final byte[] key, final byte[] value) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.sIsMember(key, value);
			}
		});
	}

	/**
	 * 取得SET所有成员
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<byte[]> smembers(final byte[] key) {
		return (Set<byte[]>) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.sMembers(key);
			}
		});
	}

	/**
	 * 设置Map
	 * 
	 * @param dbIndex
	 *            数据库
	 * @param key
	 *            键
	 * @param values
	 *            值
	 * @param expireSeconds
	 */
	@SuppressWarnings("unchecked")
	public void hmset(final int dbIndex, final byte[] key,
			final Map<byte[], byte[]> values, final int expireSeconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.select(dbIndex);
				if (key != null && key.length > 0 && !values.isEmpty()) {
					connection.hMSet(key, values);
				}
				if (expireSeconds > 0) {
					connection.expire(key, expireSeconds);
				}
				return null;
			}
		});
	}

	/**
	 * map 字段自增
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long hIncrBy(final String key, final String field, final long delta) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				long id = 0;
				try {
					id = connection.hIncrBy(key.getBytes("utf-8"),
							field.getBytes("utf-8"), delta);
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return id;
			}
		});
	}

	/**
	 * map 字段自增
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public long hIncr(final String key, final String field) {
		return hIncrBy(key, field, 1);
	}

	/**
	 * map 字段自增
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public long hDecr(final String key, final String field) {
		return hIncrBy(key, field, -1);
	}

	/**
	 * 设置Map
	 * 
	 * @param key
	 * @param values
	 * @param expireSeconds
	 */
	public void hmset(final byte[] key, final Map<byte[], byte[]> values,
			final int expireSeconds) {
		this.hmset(0, key, values, expireSeconds);
	}

	/**
	 * 获得HASH指定FIELD的所有值
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<byte[]> hmget(final byte[] key, final byte[]... values) {
		return (List<byte[]>) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						return connection.hMGet(key, values);
					}
				});
	}

	/**
	 * 设置HASH的某FIELD为某值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @param expireSeconds
	 */
	@SuppressWarnings("unchecked")
	public void hset(final byte[] key, final byte[] field, final byte[] value,
			final int expireSeconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.hSet(key, field, value);
				if (expireSeconds > 0) {
					connection.expire(key, expireSeconds);
				}
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public byte[] hget(final byte[] key, final byte[] field) {
		return (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.hGet(key, field);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public Map<byte[], byte[]> hGetAll(final byte[] key) {
		return (Map<byte[], byte[]>) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public Map<byte[], byte[]> doInRedis(
							RedisConnection connection)
							throws DataAccessException {
						return connection.hGetAll(key);
					}
				});
	}

	@SuppressWarnings("unchecked")
	public void hdel(final String key, final String field) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					connection.hDel(key.getBytes("utf-8"),
							field.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {

				}
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void set(final byte[] key, final byte[] value,
			final int expireSeconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(key, value);
				if (expireSeconds > 0)
					connection.expire(key, expireSeconds);
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public byte[] get(final byte[] key) {
		return (byte[]) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.get(key);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void del(final byte[] key) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.del(key);
				return null;
			}
		});
	}

	/**
	 * 判断一个KEY是否存在
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean exist(final byte[] key) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.exists(key);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public boolean hexist(final byte[] key, final byte[] field) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.hExists(key, field);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void publish(final String channel, final String message) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					connection.publish(channel.getBytes("utf-8"),
							message.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void hsetObj(final String key, final String field,
			final Serializable value, final int expireSeconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] bytes = SerializationUtils.serialize(value);
				try {
					connection.hSet(key.getBytes("utf-8"),
							field.getBytes("utf-8"), bytes);
					if (expireSeconds > 0) {
						connection.expire(key.getBytes("utf-8"), expireSeconds);
					}
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
				}
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> T hgetObj(final String key,
			final String field) {
		return (T) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					byte[] ret = connection.hGet(key.getBytes("utf-8"),
							field.getBytes("utf-8"));
					if (ret != null)
						return (T) SerializationUtils.deserialize(ret);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				return null;
			}
		});
	}

}
  