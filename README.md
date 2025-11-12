信用卡分析（tableauDemoV2）

以 Spring Boot 建構的信用卡帳單解析／分析網站。
特色：

支援 PDF 表格抽取 + OCR（Tess4J） 的雙路徑解析。

透過 WebSocket（STOMP） 推播「處理中/完成/失敗」進度。

前端使用 SweetAlert2 顯示處理中與錯誤訊息。

不依賴 CDN：所有前端第三方庫（SweetAlert2/SockJS/STOMP）以 固定版本 打包到 JAR。

目錄結構（重點）
src/main/
 ├─ java/...
 │   ├─ config/
 │   │   ├─ WsConfig.java               # WebSocket 設定
 │   │   └─ SecurityConfig.java         # （若有啟用）放行 /ws 與 API
 │   ├─ controller/
 │   │   └─ UploadController.java       # /api/cc/upload
 │   ├─ service/
 │   │   ├─ OcrPdfService.java          # PDFBox + Tabula + Tess4J
 │   │   └─ StatementProcessService.java# 推播進度
 │   └─ advice/
 │       └─ GlobalExceptionHandler.java # 500 錯誤轉成 JSON
 └─ resources/
     ├─ static/
     │   ├─ js/vendor/
     │   │   ├─ sweetalert2.all.v11.10.6.min.js
     │   │   ├─ sockjs.v1.6.1.min.js
     │   │   └─ stomp.umd.v7.0.0.min.js
     │   └─ css/vendor/
     │       └─ sweetalert2.v11.10.6.min.css
     └─ templates/ ...                  # Thymeleaf 頁面

需求

JDK 17+（或專案要求版本）

Maven 3.8+

Node.js（如果要在本機執行 npm install；也可改用 Maven 自動化，見下）

Tesseract 語料：chi_tra + eng（伺服器上要有 tessdata 路徑）

快速開始
# 1) 安裝前端第三方庫（固定版本、離線打包）
npm install

# 2) 啟動 Spring Boot
mvn spring-boot:run


啟動後，前端頁面在 http://localhost:8080/

檔案上傳端點：POST /api/cc/upload（回傳 jobId，之後用 WS 收進度）

前端第三方庫：固定版本（不走 CDN）

本專案在 專案根目錄 放置：

package.json（固定套件版本）

copy-vendors.js（安裝後自動複製到 resources/static）

1) package.json
{
  "name": "tableaudemov2",
  "version": "1.0.0",
  "private": true,
  "license": "MIT",
  "description": "Frontend vendor bundling for SweetAlert2 / SockJS / STOMP with pinned versions",
  "repository": {
    "type": "git",
    "url": "https://github.com/Lynn99208334/tableauDemoV2.git"
  },
  "dependencies": {
    "sweetalert2": "11.10.6",
    "sockjs-client": "1.6.1",
    "@stomp/stompjs": "7.0.0"
  },
  "scripts": {
    "postinstall": "node copy-vendors.js",
    "vendors:copy": "node copy-vendors.js"
  }
}

2) copy-vendors.js
// copy-vendors.js（放在專案根目錄）
const fs = require('fs');
const path = require('path');

const files = [
  // JS
  ['node_modules/sweetalert2/dist/sweetalert2.all.min.js',
   'src/main/resources/static/js/vendor/sweetalert2.all.v11.10.6.min.js'],
  ['node_modules/sockjs-client/dist/sockjs.min.js',
   'src/main/resources/static/js/vendor/sockjs.v1.6.1.min.js'],
  ['node_modules/@stomp/stompjs/bundles/stomp.umd.min.js',
   'src/main/resources/static/js/vendor/stomp.umd.v7.0.0.min.js'],
  // CSS
  ['node_modules/sweetalert2/dist/sweetalert2.min.css',
   'src/main/resources/static/css/vendor/sweetalert2.v11.10.6.min.css']
];

for (const [src, dst] of files) {
  const absSrc = path.resolve(src);
  const absDst = path.resolve(dst);
  fs.mkdirSync(path.dirname(absDst), { recursive: true });
  fs.copyFileSync(absSrc, absDst);
  console.log(`copied: ${src} -> ${dst}`);
}

3) 安裝／複製
npm install           # 下載固定版本並自動複製
npm run vendors:copy  # 只想重複複製時

4) HTML 引用
<link rel="stylesheet" href="/css/vendor/sweetalert2.v11.10.6.min.css"/>

<script src="/js/vendor/sockjs.v1.6.1.min.js"></script>
<script src="/js/vendor/stomp.umd.v7.0.0.min.js"></script>
<script src="/js/vendor/sweetalert2.all.v11.10.6.min.js"></script>

與 Maven 整合（CI/CD 推薦）

在 pom.xml 加入（自動下載 node/npm、跑 npm install、複製檔案）：

<plugin>
  <groupId>com.github.eirslett</groupId>
  <artifactId>frontend-maven-plugin</artifactId>
  <version>1.15.0</version>
  <configuration>
    <nodeVersion>v18.19.0</nodeVersion>
    <npmVersion>10.2.4</npmVersion>
  </configuration>
  <executions>
    <execution>
      <goals><goal>install-node-and-npm</goal></goals>
    </execution>
    <execution>
      <id>npm-install</id>
      <phase>generate-resources</phase>
      <goals><goal>npm</goal></goals>
      <configuration>
        <arguments>install --no-audit --no-fund</arguments>
      </configuration>
    </execution>
  </executions>
</plugin>


之後只要 mvn clean package，JAR 內就會包含固定版前端資產。

WebSocket（STOMP）與進度推播
伺服器端設定（WsConfig.java）
@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer {
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
  }
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/app");
  }
}

推播模型
@Data @AllArgsConstructor @NoArgsConstructor
public class ProgressMessage {
  private String jobId;
  private String status;   // PROCESSING / DONE / ERROR
  private String message;  // 顯示訊息或錯誤
  private int percent;     // 0~100
}

範例：發送進度（StatementProcessService.java）
@Service
@RequiredArgsConstructor
public class StatementProcessService {

  private final SimpMessagingTemplate mq;

  public void start(String jobId, Path pdfPath) {
    CompletableFuture.runAsync(() -> {
      try {
        send(jobId, "PROCESSING", "開始解析", 5);
        // ... OCR / Tabula 處理中 ...
        send(jobId, "PROCESSING", "OCR 中…", 35);
        // ... 更多處理 ...
        send(jobId, "DONE", "解析完成", 100);
      } catch (Exception e) {
        send(jobId, "ERROR", Optional.ofNullable(e.getMessage()).orElse("解析失敗"), 0);
      }
    });
  }

  private void send(String jobId, String status, String msg, int pct) {
    mq.convertAndSend("/topic/progress/" + jobId, new ProgressMessage(jobId, status, msg, pct));
  }
}

前端訂閱（SweetAlert2 + STOMP）
<script>
async function onNextStep() {
  try {
    const fileInput = document.querySelector('#uploadInput');
    if (!fileInput.files.length) {
      await Swal.fire({icon:'warning', title:'請先選擇檔案'}); return;
    }
    // 開啟處理中視窗
    Swal.fire({
      title: '正在處理...',
      html: '<b id="pmsg">連線中…</b><br/><progress id="pp" max="100" value="0" style="width:100%"></progress>',
      allowOutsideClick: false,
      didOpen: () => Swal.showLoading()
    });

    // 上傳取得 jobId
    const form = new FormData(); form.append('file', fileInput.files[0]);
    const resp = await fetch('/api/cc/upload', { method:'POST', body: form });
    const data = await resp.json();
    if (!resp.ok) throw new Error(data?.message || `HTTP ${resp.status}`);
    const jobId = data.jobId;

    // 建立 STOMP
    const client = new StompJs.Client({
      webSocketFactory: () => new SockJS('/ws'),
      reconnectDelay: 3000
    });

    client.onConnect = () => {
      document.getElementById('pmsg').innerText = '已連線，開始解析…';
      client.subscribe(`/topic/progress/${jobId}`, (frame) => {
        const msg = JSON.parse(frame.body);
        document.getElementById('pmsg').innerText = msg.message || '';
        const bar = document.getElementById('pp');
        if (typeof msg.percent === 'number') bar.value = msg.percent;

        if (msg.status === 'DONE') {
          Swal.fire({icon:'success', title:'完成', text: msg.message || '解析完成'})
              .then(()=> location.href = `/cc/preview?jobId=${jobId}`);
          client.deactivate();
        } else if (msg.status === 'ERROR') {
          Swal.fire({icon:'error', title:'解析失敗', text: msg.message || '未知錯誤'});
          client.deactivate();
        }
      });
    };
    client.activate();

  } catch (e) {
    Swal.fire({icon:'error', title:'操作失敗', text: e.message || '未知錯誤'});
  }
}
</script>

檔案上傳 API

POST /api/cc/upload

Request：multipart/form-data，欄位名 file

Response (200)：{"jobId":"<uuid>"}

Error (500)：由全域例外處理器回傳 {"error":"...","message":"..."}

全域例外處理（將 500 轉成 JSON）
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,Object>> handle(Exception ex) {
    Map<String,Object> body = new LinkedHashMap<>();
    body.put("error", ex.getClass().getSimpleName());
    body.put("message", Optional.ofNullable(ex.getMessage()).orElse("No message"));
    body.put("timestamp", java.time.OffsetDateTime.now().toString());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}

組態建議（application.yml）
spring:
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 20MB


若啟用 Security，請放行 /ws/**、/api/cc/**，並忽略 CSRF（或加 CSRF Token）。

OCR（Tess4J）環境

依賴建議：

net.sourceforge.tess4j:tess4j:5.11.0

org.apache.pdfbox:pdfbox-tools:3.0.2

org.apache.pdfbox:jbig2-imageio:3.0.4（掃描 PDF 常用）

伺服器需有 tessdata，包含 chi_tra.traineddata 與 eng.traineddata

程式中設定：

Tesseract t = new Tesseract();
t.setDatapath("/opt/tesseract/tessdata");
t.setLanguage("chi_tra+eng");

升級第三方庫版本

修改 package.json 版本號。

執行：

npm install
npm run vendors:copy


若檔名含版本（建議），同步更新 HTML 引用路徑。

常見問題（FAQ）

IDE 右下角跳出 “Install dependencies”？
可以按下去，等同於跑 npm install。

npm install 下載很慢/失敗
在專案根目錄新增 .npmrc 指定公司鏡像：

registry=https://<your-npm-mirror>/


打包後 404
檢查 resources/static/js/vendor 是否有檔；若沒有，跑 npm run vendors:copy。

WebSocket 連不上
確認 /ws 端點有對外、Nginx 反代有開 WebSocket、Security 已放行。

500 但前端沒訊息
確認有 GlobalExceptionHandler；前端 fetch 需解析 JSON 並把 message 丟給 SweetAlert2。

開發常用指令
# 前端資產
npm install
npm run vendors:copy

# 後端（啟動）
mvn spring-boot:run

# 打包（CI/CD 推薦）
mvn clean package

授權

本專案程式碼依專案預設授權。

第三方庫授權：SweetAlert2 / SockJS / @stomp/stompjs 皆為 MIT License。