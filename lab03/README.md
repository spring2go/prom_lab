实验三、Prometheus+Alertmanager告警实验
======

### 实验步骤

#### 1. 先决条件

* 运行Prometheus HTTP Metrics Simulator
* 运行Prometheus服务器

注意启用`--web.enable-lifecycle`，让Prometheus支持通过web端点动态更新配置

#### 2. HttpSimulatorDown告警

在Prometheus目录下:

添加`simulator_alert_rules.yml`告警配置文件

```
groups:
- name: simulator-alert-rule
  rules:
  - alert: HttpSimulatorDown
    expr: sum(up{job="http-simulator"}) == 0
    for: 1m
    labels:
      severity: critical
```

修改`prometheus.yml`，引用`simulator_alert_rules.yml`文件

```
# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  - "simulator_alert_rules.yml"
  # - "second_rules.yml"

```

通过Postman动态更新Prometheus配置
```
curl -X POST http://PROMETHEUS_URL:9090/-/reload
```

通过`Prometheus->Status`的`Configuration`和`Rules`确认配置和告警设置生效

关闭`Prometheus HTTP Metrics Simulator`应用，通过`Prometheus->Alert`界面上查看告警触发情况


#### 3. ErrorRateHigh告警

假设已经执行上面的步骤2，则重新运行Prometheus HTTP Metrics Simulator

在`simulator_alert_rules.yml`文件中增加告警配置

```
  - alert: ErrorRateHigh
    expr: sum(rate(http_requests_total{job="http-simulator", status="500"}[5m])) / sum(rate(http_requests_total{job="http-simulator"}[5m])) > 0.02
    for: 1m
    labels:
      severity: major
    annotations:
      summary: "High Error Rate detected"
      description: "Error Rate is above 2% (current value is: {{ $value }}"
```

通过Postman动态更新Prometheus配置
```
curl -X POST http://PROMETHEUS_URL:9090/-/reload
```

通过`Prometheus->Status`的`Configuration`和`Rules`确认配置和告警设置生效

通过Postman调高HTTP Simulator的错误率到10%
```
curl -X POST http://localhost:8080/error_rate/10
```

通过`Prometheus->Graph`界面校验错误率上升

通过`Prometheus->Alert`界面校验查看触发情况

#### 4. 安装和配置Alertmanager

下载[Alertmanager 0.15.2 for Windows](https://github.com/prometheus/alertmanager/releases/download/v0.15.2/alertmanager-0.15.2.windows-amd64.tar.gz)，并解压到本地目录。

在`Alertmanager`目录下修改`alertmanager.yml`文件：
```
global:
  smtp_smarthost: 'smtp.163.com:25'
  smtp_from: 'xxxxx@163.com'
  smtp_auth_username: 'xxxxx@163.com'
  smtp_auth_password: 'xxxxx'

route:
  group_interval: 1m
  repeat_interval: 1m
  receiver: 'mail-receiver'
receivers:
- name: 'mail-receiver'
  email_configs:
    - to: 'xxxxxx@163.com' 

```

启动Alertmanager
```
./alertmanager.exe
```

在`Prometheus`目录下，修改`prometheus.yml`配置Alertmanager地址
```
# Alertmanager configuration
alerting:
  alertmanagers:
  - static_configs:
    - targets:
      - localhost:9093
```

通过Postman动态更新Prometheus配置
```
curl -X POST http://PROMETHEUS_URL:9090/-/reload
```

通过`Prometheus->Status`的`Configuration`和`Rules`确认配置和告警设置生效

通过Alertmanager UI界面和设置的邮箱，校验`ErrorRateHigh`告警触发

Alertmanager UI访问地址：
```
http://localhost:9093
```





