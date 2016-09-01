# almost-universal-relationship by mysql

in almost way, when we need to store one to many relationship in mysql, we always use this

each one person may have many social networks, like facebook, twitter, line, weChat etc, we can build model and design mysql like this.

	select * from t_user where id = 1;

	+----+-------+------+
	| id | name  | age  |
	+----+-------+------+
	|  1 | allen |   15 |
	+----+-------+------+

	select * from t_social_network where id in (7, 8);

	+----+-------+
	| id | name  |
	+----+-------+
	|  7 | line  |
	+----+-------+
	|  8 | twitter  |
	+----+-------+

	select * from t_user_social where user_id = 1;
	+---------+-----------+
	| user_id | social_id |
	+----+---------+------+
	|       1 |         7 |
	|       1 |         8 |
	+---------+-----------+


but, whether it is too troublesome. you need to create table and build relationship between models.

however, there is a way to slove this troublesome problem.

you can only do it like this.

you just only add this:
#
	@RelatedField(relatedType = RelatedType.TEST, runtimeClass = ArrayList.class)
	private List<Long> socialNetworkIds;
#
in your User.java. And you need not create table t_user_social

#
    @RelatedObject(objectType = ObjectType.User)
    public class User {
	    private Long id;
	    private String name;
	    private Integer age;

	    @RelatedField(relatedType = RelatedType.USER_SOCIAL_NETWORK_IDS, runtimeClass = ArrayList.class)
	    private List<Long> socialNetworkIds;
    }
#

#
    public enum RelatedType {
        TEST(101, "socialNetworkIds");  // pay attetion, the TEST(101, "socialNetworkIds") enum's second value is name of field
                                        // private List<Long> socialNetworkIds in User.java
    }
#

#
    disadvantage:
    1. in enum RelatedType, the string value is name of field in *.java
    2. not support field which do not have default constructor
#


    环境要求
    该project依赖https://github.com/TigerPengPeng/async-event.git,
    所以, 需要git clone https://github.com/TigerPengPeng/async-event.git 到本地, 然后执行
    mvn clean install -DskipTests -U
    将async-event install 到本地仓库

    需要能连接到rabbitmq-server, 配置信息在文件
        sys.rabbitmq.config.properties
    中更改

    mvn build
       mvn clean install -DskipTests -U
       or
       mvn clean install -Dscale=/ (if you can connect to your rabbitmq server)

    源码在src目录下
    demo在test目录下

    开发环境补充:
        ide: IDEA
        要求: 安装插件lombok
