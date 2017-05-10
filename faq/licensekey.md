# License Key Frequently Asked Questions


## How do I load a license key?
You'll need two things:  
1. A license key file.  
2. A version of the license key library. 

The library is a jar or dll that you add to your project. This library is used to load and validate your license key. This library will also enable the functionality in the iText 7 add-ons if there is an appropriate entry in the license key file.

To load a license key, use the following line of code:

**Java**
```Java
LicenseKey.loadLicenseFile("path/to/itextkey.xml");
```

**C#**
```C#
LicenseKey.LoadLicenseFile("path/to/itextkey.xml");
```

## How do I check if my key has been loaded correctly?
For the core iText library, the license is only checked when you load it. If you loaded the license key and there was no exception thrown, then it loaded perfectly fine. The add-ons will regularly check the license key. If there is an issue with your license key, the library will throw a `LicenseKeyException`. The possible exceptions and how to resolve them are described below.

If you want to check the license key manually, you can call the following method:

**Java**
```Java
LicenseKey.scheduledCheck(null);
```

**C#**
```C#
LicenseKey.ScheduledCheck(null);
```

This will throw a `LicenseKeyException` when the key is invalid or corrupt.


## Which version of the license key library should I use?
If you're using iText 7, you'll need to use version 2.x.y of the license key library.  
If you're using iText 5, you'll need to use version 1.x.y of the license key library.

You **can't** mix the versions. Don't use license key 2.x.y with iText 5.x.y or license key 1.x.y with iText 7.x.y. This will not work!

Please use the latest version within the appropriate major version to have the latest features and bug fixes available.


## Where can I download the license key library?
You can download the library manually through the following channels:
- Java: 
  - 1.x.y: [https://repo.itextsupport.com/list/releases/com/itextpdf/tool/itext-licensekey/][licensejava1]
  - 2.x.y: [https://repo.itextsupport.com/list/releases/com/itextpdf/itext-licensekey/][licensejava2]
- .NET:
  - 1.x.y: [https://www.nuget.org/packages/itextsharp.licensekey/][licensedotnet1]
  - 2.x.y: [https://www.nuget.org/packages/itext7.licensekey/][licensedotnet2]
  
Or you can use one of our supported dependency managers:  

### Maven
To add the license key library dependency to your project you'll first need to add our repository to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>itext</id>
        <name>iText Repository - releases</name>
        <url>https://repo.itextsupport.com/releases</url>
    </repository>
</repositories>
```

After which you can add the license key as a dependency:

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-licensekey</artifactId>
    <version>2.x.y</version>
</dependency>
```

Or if you want to load version 1:

```xml
<dependency>
    <groupId>com.itextpdf.tool</groupId>
    <artifactId>itext-licensekey</artifactId>
    <version>1.x.y</version>
</dependency>
```

### Nuget
We added our license key library to Nuget.

- To install the iText 7 License Key Library (2.x.y), run the following command in the Package Manager Console:
```
Install-Package itext7.licensekey
```
- To install the iText 5 License Key Library (1.x.y), run the following command in the Package Manager Console:
```
Install-Package itextsharp.licensekey
```

## Can I use my iText 5 license with iText 7?
No you can't. Doing so will throw a `LicenseKeyException` and you won't be able to use it. This is also true for the reverse, you can't use an iText 7 license for iText 5.

If you do want to have a license for the other version, please contact our [sales][sales] department.


## I upgraded to iText 7, how do I use my license
The same way you loaded the iText 5 license. You use line of code mentioned above. You do need to be aware of the following:
- You need to use iText 7
- You need to load your iText 7 license file and disregard your iText 5 license
- You need to use the license key library version 2.x.y


## I get "version mismatch error"
You are most likely combining the wrong library version with your license key. If you bought an iText 5 license, please use version 1.x.y. If you bough an iText 7 license, please use 2.x.y.


## I get "signature corrupted"
This can mean a few things:
- The license key file was corrupted during the download. Try downloading it again and loading the newly downloaded file. If the issue still persists, [contact us][sales] so that we can resolve this issue.
- You changed the contents of your license key. Please revert to the original state of the license key. If you want to change the content of the key, please contact our [sales][sales] department.


## I get "product not found"
You are trying to use an add on product without having loaded a license key that contains a valid license for that product. If you did buy a license for the add-on, please contact our [sales][sales] department to rectify this. If you didn't buy a license and you want to try out the add on, you may want to register for a [trial license][trial].


## I get "The license info of already loaded license doesn't match the new one."
You are most likely trying to load an additional license key containing new add ons. This can only work if the core information is the same. If it's not the same and it should be, [contact us][sales] to rectify this issue. If it's not the same, please only use your own license key and don't pass your license keys around.

[licensejava1]: https://repo.itextsupport.com/list/releases/com/itextpdf/tool/itext-licensekey/
[licensejava2]: https://repo.itextsupport.com/list/releases/com/itextpdf/itext-licensekey/
[licensedotnet1]: https://www.nuget.org/packages/itextsharp.licensekey/
[licensedotnet2]: https://www.nuget.org/packages/itext7.licensekey/
[sales]: http://itextpdf.com/sales
[trial]: http://pages.itextpdf.com/iText-7-Free-Trial-Landing-Page-1.html