# ハンズオン用 Rolling dice Web アプリケーション

DevOps（CI/CD、オブザーバビリティー）をはじめとする各種ハンズオンでアプリケーションレイヤーで利用する Web アプリケーションです。

## Web アプリケーション構成

アプリケーションをシーケンス図で示します。

```mermaid
sequenceDiagram
    actor User
    participant webui
    participant webapi
    participant Database

    User->>webui: http://localhost:8081/
    Note right of User: Query String<br>- sleep: milliseconds to sleep<br>- loop: number of times to loop<br>- error: flag to cause HTTP status error
    activate webui

        webui->>webapi: http://localhost:8082/api/dice/v1/roll
        activate webapi

            alt sleep != null
                webapi->>webapi: Sleep process
            end
            alt loop != null
                loop loop
                    webapi->>webapi: File access process
                end
            end
            alt error == 1
                webapi->>webapi: Trigger exception (terminates WebAPI)
            end
            webapi->>webapi: Roll the dice
            webapi->>Database: INSERT dice roll result
            activate Database

                Database-->>webapi: INSERT successful

            deactivate Database
            webapi-->>webui: Dice roll result

        deactivate webapi
        webui->>webapi: http://localhost:8082/api/dice/v1/list
        activate webapi

            webapi->>Database: SELECT dice roll history
            activate Database

            Database-->>webapi: Dice roll history
            
            deactivate Database
            webapi-->>webui: Dice roll history

        deactivate webapi
        webui->>webui: Generate HTML page with result and history
        webui-->>User: HTML page

    deactivate webui
```



## 改修手順

### Web アプリケーション起動



