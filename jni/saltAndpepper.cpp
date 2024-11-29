#include <iostream>
#include <iomanip>
#include <random>
#include <string>
#include <openssl/evp.h> // Include OpenSSL EVP library for cryptographic operations
#include <openssl/sha.h> // For SHA256_DIGEST_LENGTH (constant)
#include <cstdlib>       // For rand() and srand()
#include <ctime>         // For time()

using namespace std;

class SaltAndPepper
{
public:
    static string pepperGenerator()
    {
        srand(static_cast<unsigned int>(time(0)));

        int c1 = (rand() % (100 - 42 + 1)) + 42;
        int c2 = (rand() % (127 - 100 + 1)) + 100;
        int c3 = (rand() % (54 - 32 + 1)) + 32;

        string pepper = "";
        // pepper += ch1 + ch2 + ch3;
        char ch1 = static_cast<char>(c1);
        char ch2 = static_cast<char>(c2);
        char ch3 = static_cast<char>(c3);

        pepper += ch1; // Add the first character
        pepper += ch2; // Add the second character
        pepper += ch3; // Add the third character
        return pepper;
    }

    // using sha256

    static string hasher(const string &input)
    {
        // Create a buffer to hold the hash (SHA256 produces a 256-bit or 32-byte hash)
        unsigned char hash[SHA256_DIGEST_LENGTH];

        // Initialize EVP_MD_CTX context for the hash
        EVP_MD_CTX *ctx = EVP_MD_CTX_new(); // Creates a new EVP_MD_CTX context
        if (ctx == nullptr)
        {
            cerr << "Error creating context" << endl; // Error handling in case context creation fails
            return "";
        }

        // Initialize the SHA-256 algorithm
        if (EVP_DigestInit_ex(ctx, EVP_sha256(), nullptr) != 1)
        {
            cerr << "Error initializing SHA-256" << endl;
            EVP_MD_CTX_free(ctx); // Free the context if initialization fails
            return "";
        }

        // Update the context with the input data
        if (EVP_DigestUpdate(ctx, input.c_str(), input.length()) != 1)
        {
            cerr << "Error updating SHA-256" << endl;
            EVP_MD_CTX_free(ctx); // Free the context if update fails
            return "";
        }

        // Finalize the digest (compute the hash)
        unsigned int length;
        if (EVP_DigestFinal_ex(ctx, hash, &length) != 1)
        {
            cerr << "Error finalizing SHA-256" << endl;
            EVP_MD_CTX_free(ctx); // Free the context if finalizing fails
            return "";
        }

        // Clean up the context
        EVP_MD_CTX_free(ctx);

        // Convert the hash to a hexadecimal string
        stringstream ss;
        for (int i = 0; i < SHA256_DIGEST_LENGTH; i++)
        {
            ss << hex << setw(2) << setfill('0') << (int)hash[i]; // Convert each byte to a 2-digit hex string
        }
        return ss.str(); // Return the hexadecimal string representation of the hash
    }

    static bool validatePasswordWithLoops(const std::string &originalHashedPassword, const std::string &restOfString)
    {
        const int range1_start = 42, range1_end = 100;
        const int range2_start = 100, range2_end = 127;
        const int range3_start = 32, range3_end = 54;

        for (int char1 = range1_start; char1 <= range1_end; ++char1)
        {
            for (int char2 = range2_start; char2 <= range2_end; ++char2)
            {
                for (int char3 = range3_start; char3 <= range3_end; ++char3)
                {
                    char ch1 = static_cast<char>(char1);
                    char ch2 = static_cast<char>(char2);
                    char ch3 = static_cast<char>(char3);

                    string prefix = "";
                    prefix += ch1; // Add the first character
                    prefix += ch2; // Add the second character
                    prefix += ch3; // Add the third character
                    // string hashedPart = hasher(restOfString);
                    string generatedPassword = prefix + restOfString;
                    string hashedPart = hasher(generatedPassword);
                    if (hashedPart == originalHashedPassword)
                    {
                        cout << "Valid password found: " << generatedPassword << "\n";
                        return true;
                    }
                }
            }
        }

        cout << "No valid password found.\n";
        return false;
    }
};

int main()
{
    string input = "jelo";
    string myPepper = SaltAndPepper::pepperGenerator() + input;
    cout << myPepper << endl;
    string myHashedPassword = SaltAndPepper::hasher(myPepper);
    cout << myHashedPassword << endl;
    string hacker = "jelo";
    SaltAndPepper::validatePasswordWithLoops(myHashedPassword, hacker);
    // cout << "SHA-256 hash of '" << input << "': " << myHashedPassword << endl; // Display the result

    return 0;
}