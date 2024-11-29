#include <iostream>
#include <openssl/hmac.h>
#include <openssl/sha.h>
#include <string>
#include <sstream>
#include <ctime>
#include <stdexcept>
#include <vector>
#include <algorithm>

class JWT
{
public:
    // Base64URL Decoding
    static std::string base64UrlDecode(const std::string &input)
    {
        std::string decoded = input;
        // Replace URL-safe characters
        std::replace(decoded.begin(), decoded.end(), '-', '+');
        std::replace(decoded.begin(), decoded.end(), '_', '/');
        // Add padding if necessary
        while (decoded.size() % 4 != 0)
        {
            decoded += '=';
        }

        std::string result;
        BIO *bio = BIO_new_mem_buf(decoded.data(), decoded.size());
        BIO *b64 = BIO_new(BIO_f_base64());
        BIO_set_flags(b64, BIO_FLAGS_BASE64_NO_NL);
        bio = BIO_push(b64, bio);

        char buffer[256];
        int bytesRead = 0;
        while ((bytesRead = BIO_read(bio, buffer, 256)) > 0)
        {
            result.append(buffer, bytesRead);
        }
        BIO_free_all(bio);

        return result;
    }

    // HMAC-SHA256 Signing
    static std::string hmacSha256(const std::string &data, const std::string &key)
    {
        unsigned char *digest;
        unsigned int len = SHA256_DIGEST_LENGTH;

        digest = HMAC(EVP_sha256(), key.c_str(), key.length(),
                      reinterpret_cast<const unsigned char *>(data.c_str()), data.length(),
                      nullptr, nullptr);

        return base64UrlEncode(digest, len);
    }

    // Base64URL Encoding
    static std::string base64UrlEncode(const unsigned char *data, size_t len)
    {
        static const char encodeTable[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        std::string encoded;
        size_t i = 0;

        for (; i + 2 < len; i += 3)
        {
            encoded += encodeTable[(data[i] >> 2) & 0x3F];
            encoded += encodeTable[((data[i] & 0x3) << 4) | ((data[i + 1] >> 4) & 0xF)];
            encoded += encodeTable[((data[i + 1] & 0xF) << 2) | ((data[i + 2] >> 6) & 0x3)];
            encoded += encodeTable[data[i + 2] & 0x3F];
        }

        if (i < len)
        {
            encoded += encodeTable[(data[i] >> 2) & 0x3F];
            if (i + 1 < len)
            {
                encoded += encodeTable[((data[i] & 0x3) << 4) | ((data[i + 1] >> 4) & 0xF)];
                encoded += encodeTable[(data[i + 1] & 0xF) << 2];
            }
            else
            {
                encoded += encodeTable[(data[i] & 0x3) << 4];
            }
        }

        return encoded;
    }

    // Generate JWT
    static std::string generateToken(const std::string &secretKey, const std::string &type, const std::string &id)
    {
        // Header
        std::string header = R"({"alg":"HS256","typ":"JWT"})";
        std::string encodedHeader = base64UrlEncode(reinterpret_cast<const unsigned char *>(header.c_str()), header.length());

        // Payload
        std::stringstream payloadStream;
        payloadStream << R"({"type":")" << type << R"(","id":")" << id << R"(",)";
        payloadStream << R"("iat":)" << std::time(nullptr) << ",";
        payloadStream << R"("exp":)" << (std::time(nullptr) + 3600) << "}"; // Expires in 1 hour
        std::string payload = payloadStream.str();
        std::string encodedPayload = base64UrlEncode(reinterpret_cast<const unsigned char *>(payload.c_str()), payload.length());

        // Signature
        std::string data = encodedHeader + "." + encodedPayload;
        std::string signature = hmacSha256(data, secretKey);

        // JWT
        return data + "." + signature;
    }

    // Decode JWT
    static std::string decodeToken(const std::string &token, const std::string &secretKey)
    {
        // Split token into parts
        std::vector<std::string> parts;
        std::stringstream ss(token);
        std::string item;
        while (std::getline(ss, item, '.'))
        {
            parts.push_back(item);
        }

        if (parts.size() != 3)
        {
            throw std::invalid_argument("Invalid JWT format");
        }

        // Decode header and payload
        std::string header = base64UrlDecode(parts[0]);
        std::string payload = base64UrlDecode(parts[1]);
        std::string signature = parts[2];

        // Verify signature
        std::string data = parts[0] + "." + parts[1];
        std::string computedSignature = hmacSha256(data, secretKey);

        if (computedSignature != signature)
        {
            throw std::invalid_argument("Invalid JWT signature");
        }

        return payload;
    }
};

int main()
{
    std::string secretKey = "your_secret_key";
    std::string type = "student"; // Can be "student" or "staff"
    std::string id = "1234567890";

    // Generate JWT
    std::string jwt = JWT::generateToken(secretKey, type, id);
    std::cout << "Generated JWT: " << jwt << std::endl;

    // Decode JWT
    try
    {
        std::string payload = JWT::decodeToken(jwt, secretKey);
        std::cout << "Decoded Payload: " << payload << std::endl;
    }
    catch (const std::exception &e)
    {
        std::cerr << "Error decoding JWT: " << e.what() << std::endl;
    }

    return 0;
}
