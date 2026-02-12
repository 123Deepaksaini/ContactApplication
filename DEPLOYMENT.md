# Render Deployment Guide - Contact Application

## Prerequisites
- GitHub account with the project repository
- Render account (free tier available at https://render.com)
- MySQL database (Render provides MySQL or you can use Railway.app)

---

## Step 1: Prepare GitHub Repository

1. **Push your project to GitHub**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/Contact-Application.git
   git push -u origin main
   ```

2. **Ensure these files are in your repo root:**
   - `Dockerfile` (already created)
   - `.dockerignore` (already created) 
   - `pom.xml`
   - `schema.sql`
   - `src/` folder

---

## Step 2: Setup MySQL Database

You have 2 options:

### Option A: Use Railway.app (Recommended for free tier)
1. Go to https://railway.app
2. Sign in with GitHub
3. Click "New Project" → "Provision MySQL"
4. Copy the following environment variables:
   - `MYSQLHOST`
   - `MYSQLPORT`
   - `MYSQLDATABASE`
   - `MYSQLUSER`
   - `MYSQLPASSWORD`

5. Run the schema.sql on Railway MySQL:
   - Use Railway Dashboard → MySQL → Connect
   - Copy-paste contents of `schema.sql` and execute

### Option B: Use Render MySQL
1. Go to https://render.com
2. Dashboard → Create New → Database → MySQL
3. Copy credentials after database is created

---

## Step 3: Deploy on Render

### Method 1: Using Docker Image (Recommended)

1. **Go to https://render.com** and sign in
2. **Create New Service** → **Web Service**
3. **Connect to GitHub**
   - Select "Connect account"
   - Authorize Render to access your repos
   - Select your repository

4. **Configure Service**
   - **Name**: `contact-app` (or any name)
   - **Runtime**: `Docker`
   - **Region**: `Singapore` (or nearest)
   - **Plan**: `Free` (0.5 CPU, 512MB RAM)
   - **Branch**: `main`
   - **Build Command**: (leave blank - uses Dockerfile)
   - **Start Command**: (leave blank - uses Dockerfile)

5. **Add Environment Variables**
   - Click "Advanced" → "Environment Variables"
   - Add the following variables from your MySQL database:
   
   ```
   MYSQLHOST=xxx.mysql.railway.app
   MYSQLPORT=3306
   MYSQLDATABASE=railway
   MYSQLUSER=root
   MYSQLPASSWORD=xxxxx
   PORT=8080
   ```

6. **Deploy**
   - Click "Create Web Service"
   - Wait for deployment to complete (5-10 minutes)
   - Your app URL will be shown: `https://contact-app.onrender.com`

---

## Step 4: Verify Deployment

1. Go to your Render dashboard
2. Check the build logs - should show "Deployment live" ✅
3. Visit your app URL
4. Test login with default credentials:
   - **Username**: `admin` or `deepak`
   - **Password**: `admin123` or `deepak123`

---

## Step 5: Enable Auto-Deployment

1. In Render Dashboard → Select your service
2. **Settings** → **Auto-Deploy**
3. Toggle "Auto-deploy commit on push" → **On**
4. Now every push to GitHub will auto-deploy!

---

## Troubleshooting

### Build fails with "Maven not found"
- Ensure `pom.xml` is in project root
- Check Dockerfile includes Maven

### Database connection fails
- Verify environment variables match exactly
- Test connection string: `jdbc:mysql://HOST:PORT/DATABASE`
- Check MySQL is accessible from Render IP

### Java heap memory error
- Render free tier has 512MB RAM
- Add to Dockerfile ENTRYPOINT:
  ```
  java -Xmx256m -jar app.jar
  ```

### Port issues
- Ensure `server.port=${PORT:8080}` in application.properties
- Render automatically sets PORT variable

---

## Local Testing Before Deploy

```bash
# Build Docker image locally
docker build -t contact-app .

# Run locally with environment variables
docker run -p 8080:8080 \
  -e MYSQLHOST=localhost \
  -e MYSQLPORT=3306 \
  -e MYSQLDATABASE=railway \
  -e MYSQLUSER=root \
  -e MYSQLPASSWORD=password \
  contact-app
```

---

## Deployment Checklist

- [ ] GitHub repo created and code pushed
- [ ] Dockerfile and .dockerignore in repo root
- [ ] MySQL database created (Railway or Render)
- [ ] schema.sql executed on MySQL
- [ ] Render account created
- [ ] Environment variables configured
- [ ] Deployment completed successfully
- [ ] Login tested with credentials
- [ ] Auto-deploy enabled

---

## Post-Deployment

### Update Default Credentials (IMPORTANT)
Go to admin dashboard and change admin password immediately!

### Monitor Logs
- Render Dashboard → Logs tab
- Check for errors and performance

### Enable Backups
- Set up MySQL automated backups on Railway

---

## Cost Estimates (as of Feb 2026)

- **Render Web Service**: Free (may suspend after 15 min inactivity)
- **Railway MySQL**: $5/month (~10GB storage)
- **Total**: ~$5/month for production

---

## Support Links

- Render Docs: https://render.com/docs
- Spring Boot Docs: https://spring.io/projects/spring-boot
- Railway Docs: https://docs.railway.app
- Docker Docs: https://docs.docker.com

