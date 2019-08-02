Profile
=======

## 1. profile简介
profile可以定义一系列配置,然后指定其激活条件.这样我们就可以在不同环境下激活不同的maven配置信息.

例如:我们可以通过profile在普通集群以及最小资源集群下配置不同的配置信息

### 1.1 profile定义位置
对于maven3,可以在不同地方定义profile,所作用的范围也不一样
1. 针对特定项目的profile配置,可以在定义在项目所在的`pom.xml`
2. 针对特定用户的profile配置,可以在用户目录下的`setting.xml`文件中定义`pom.xml`,该文件在用户家目录下的`.m2`目录下
3. 全局的profile配置,可以直接在`${maven_home}`下的`conf/setting.xml`中配置

### 1.2 profile中能定义的信息
对于profile所处位置,profile文件中能定义的信息也是不一样的.以下就分两种情况来讨论:
1. 定义在`setting.xml`文件中
2. 定义在`pom.xml`文件中

#### 1.2.1 profile定义在`setting.xml`文件中
当profile定义在`setting.xml`文件中,就意味着这个profile的配置是全局的,它会对所有项目或者某一个特定的用户生效.
因为它是全局的,所以在sett.xml中只能定义一些相对而言范围宽泛的配置信息,比如远程仓库.
而比较细粒度的一些配置信息需要根据项目的不同来定义在项目的`pom.xml`文件中.
具体而言,能够定义在`setting.xml`文件中的信息由`<repositories>`,`<pluginRepositories>`和`<properties>`,
定义在`<properties>`里面的键值对可以在`pom.xml`文件中使用

#### 1.2.2 profile定义在`pom.xml`文件中
定义在`pom.xml`文件中的profiles可以定义很多信息.主要有以下这些
1. `<repositories>`
2. `<pluginRepositories>`
3. `<dependencies>`
4. `<build>`
5. `<plugins>`
6. `<properties>`
7. `<dependencyManagement>`
8. `<distributionManagement>`
9. 以及build下面的一些子元素,`<defaultGoal>`,`<resoures>`,`<finalName>`

### 1.3 profile的激活方式
maven给我们提供了很多种不同的激活方式.
比如,可以使用`-p`参数显示的激活一个profile,也可以条件环境的设置让它自动激活
 
#### 1.3.1 使用`activeByDefault`设置激活
示例:
```
<profiles>

    <profile>
        <id>profileTest1</id>
        <properties>
            <hello>world</hello>
        </properties>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    
    <profile>
        <id>profileTest2</id>
        <properties>
            <hello>andy</hello>
        </properties>
    </profile>

</profiles>
```

在profile中可以使用`activation`来指定激活条件,当没有指定激活条件时,然后制定`activationByDefault`为`true`的时候就表示
没有指定其他profile激活状态时,该profile就会激活,所以当我们使用`maven package`的时候上面的`profileTest1`就会被激活,
但是,当我们使用`mvm package -P profileTest2`的时候就仅仅会激活`profileTest2`.

#### 1.3.3 在`setting.xml`文件中使用`activationProfile`来指定处于激活状态的profile
```
<profiles>

    <profile>
        <id>profileTest1</id>
        <properties>
            <hello>world</hello>
        </properties>
    </profile>
    
    <profile>
        <id>profileTest2</id>
        <properties>
            <hello>andy</hello>
        </properties>
    </profile>

</profiles>
```
上面的profiles可以在定义在`setting.xml`中或者`pom.xml`中.这时候如果我们指定profileTest1为激活状态,那么我们可以在`setting.xml`中定义`activationProfile`,如下
```
<activationProfiles>
    <activeProfile>profileTest1</activeProfile>
</activationProfiles>
```
当然可以在`activationProfiles`中指定多个激活的`activeProfile`
```
<activationProfiles>
    <activeProfile>profileTest1</activeProfile>
    <activeProfile>profileTest2</activeProfile>
</activationProfiles>
```

在`profileTest1`和`profileTest2`中,定义了共同属性`hello`,在`pom.xml`文件中如果要使用`hello`属性值的时候,是根据`actiProfile`激活的顺序来进行覆盖取值的,
也就是后面定义的会覆盖前面定的

#### 1.3.4 使用`-P`参数显示的激活一个profile
```
<profiles>

    <profile>
        <id>profileTest1</id>
        <properties>
            <hello>world</hello>
        </properties>
    </profile>
    
    <profile>
        <id>profileTest2</id>
        <properties>
            <hello>andy</hello>
        </properties>
    </profile>

</profiles>
```
在命令行打包时,我们可以使用`-P`参数显示的选择性的激活一个profile
- 激活profileTest1: `mvn package -P profileTest1`
- 激活profileTest2: `mvn package -P profileTest2`

当使用`activationByDefault`或者`setting.xml`中定义了处于激活状态的profile, 
但是我们在进行某些操作的时候不希望它被激活,可以这样做
- mvn package -P !profileTest1

#### 1.3.5 根据环境来激活profile
profile一个很重要的特性就是根据不同的环境来激活,比如根据不同的操作系统来激活profile,或者根据不同的jdk版本来激活

##### 1.3.5.1 根据jdk来激活不同的profile
```
<profiles>

    <profile>
        <id>profileTest1</id>
        <jdk>1.8</jdk>
        <properties>
            <hello>world</hello>
        </properties>
    </profile>
    
    <profile>
        <id>profileTest2</id>
        <jdk>[1.5,1.8)</jdk>
        <properties>
            <hello>andy</hello>
        </properties>
    </profile>

</profiles>
```
当jdk版本为1.8的时候激活profileTest1
当jdk版本为1.5,1.6,1.7的时候激活profileTest2

##### 1.3.5.2 根据操作系统来激活不同的profile
```
<profiles>

    <profile>
        <id>profileTest1</id>
        <activation>
            <os>
                <name>Windows XP</name>
                <family>Windows<family>
                <arch>x86</arch>
                <version>5.1.2600</version
            </os>
        </activation>
        <properties>
            <hello>world</hello>
        </properties>
    </profile>

</profiles>
```
上面情况就是根据操作系统的分类来激活profileTest1

##### 1.3.5.3 根据系统属性来激活不同的profile
```
<profiles>

    <profile>
        <id>profileTest1</id>
        <activation>
            <property>
                <name>hello</name>
                <value>test1</value>
            </property>
        </activation>
    </profile>
    
    <profile>
        <id>profileTest1</id>
        <activation>
            <property>
                <name>hello</name>
                <value>test2</value>
            </property>
        </activation>
    </profile>
   
</profiles>
```
在`mavenDemo-profile`模块下`resources`中创建`test.properties`文件并定义`value=${hello}`
然后基于以下两种指定不同系统属性情况查看value值:
1. `mvn package -Dhello=test1`,打包完成后可以在`target/classes/test.properties`文件中看到`value=test1`
2. `mvn package -Dhello=test2`,打包完成后可以在`target/classes/test.properties`文件中看到`value=test2`

##### 1.3.5.4 根据文件是否存在激活不同的profile
```
<profiles>

   <profile>
        <id>profileTest1</id>
        <activation>
            <file>
                <exists>target</exists>
            </file>
        </activation>
    </profile>

</profiles>
```
以上定义表示当target文件存在时激活profileTest1

```
<profiles>

   <profile>
        <id>profileTest1</id>
        <activation>
            <file>
                <missing>target</missing>
            </file>
        </activation>
    </profile>

</profiles>
```
以上定义表示当target文件不存在时激活profileTest1

###1.4 查看当前处于激活状态的profile
在建立项目的过程中,到底哪一个profile是处于激活状态?maven为我们提供了一个指令可以查看当前处于激活状态的profile都有哪些,
这个指令就是: `mvn help:active-profiles`

### 2 profile使用示例
```
<profiles>
    <!--普通集群模式-->
    <profile>
        <id>cluster</id>
        <properties>
            <package-mode-runtime>runtime</package-mode-runtime>
            <package-mode-provided>provided</package-mode-provided>
        </properties>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-shade-plugin</artifactId>
                    <version>3.0.0</version>
                    <executions>
                        <execution>
                            <phase>package</phase>
                            <goals>
                                <goal>shade</goal>
                            </goals>
                            <configuration>
                                <artifactSet>
                                    <excludes>
                                        <exclude>org.apache.flink:force-shading</exclude>
                                        <exclude>com.google.code.findbugs:jsr305</exclude>
                                        <exclude>org.slf4j:*</exclude>
                                        <exclude>log4j:*</exclude>
                                    </excludes>
                                </artifactSet>
                                <filters>
                                    <filter>
                                        <!-- Do not copy the signatures in the META-INF folder.
                                        Otherwise, this might cause SecurityExceptions when using the JAR. -->
                                        <artifact>*:*</artifact>
                                        <excludes>
                                            <exclude>META-INF/*.SF</exclude>
                                            <exclude>META-INF/*.DSA</exclude>
                                            <exclude>META-INF/*.RSA</exclude>
                                        </excludes>
                                    </filter>
                                </filters>
                                <transformers>
                                    <transformer
                                            implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                        <mainClass>com.github.tsingjyujing.lofka.nightwatcher.StreamingEntry
                                        </mainClass>
                                    </transformer>
                                </transformers>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>

        <!--最小集群模式-->
        <profile>
            <id>mini-cluster</id>
            <properties>
                <package-mode-runtime>compile</package-mode-runtime>
                <package-mode-provided>compile</package-mode-provided>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>

            <build>
                <plugins>

                    <!--用于将依赖复制到lib文件夹下-->
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-dependency-plugin</artifactId>
                        <executions>
                            <execution>
                                <id>copy-dependencies</id>
                                <phase>prepare-package</phase>
                                <goals>
                                    <goal>copy-dependencies</goal>
                                </goals>
                                <configuration>
                                    <outputDirectory>${project.build.directory}/lib</outputDirectory>
                                    <overWriteReleases>false</overWriteReleases>
                                    <overWriteSnapshots>false</overWriteSnapshots>
                                    <overWriteIfNewer>true</overWriteIfNewer>
                                    <includeScope>compile</includeScope>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>

                    <!--打包使用插件-->
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-jar-plugin</artifactId>
                        <configuration>
                            <archive>
                                <manifest>
                                    <addClasspath>true</addClasspath>
                                    <classpathPrefix>lib/</classpathPrefix>
                                    <mainClass>com.github.tsingjyujing.lofka.nightwatcher.StreamingEntry</mainClass>
                                </manifest>
                            </archive>
                        </configuration>
                    </plugin>

                </plugins>
            </build>
        </profile>
 </profiles>
 ```