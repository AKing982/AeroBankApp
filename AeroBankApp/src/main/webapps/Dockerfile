# Build stage

FROM node:14 as build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# Production stage
FROM node:14-alpine

WORKDIR /app

# Install serve to run the application
RUN npm install -g serve

# Copy build files from the build stage
COPY --from=build /app/build ./build

# Expose port 3000
EXPOSE 3000

# Start the app using serve
CMD ["serve", "-s", "build", "-l", "3000"]