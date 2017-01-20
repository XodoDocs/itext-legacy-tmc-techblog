Where and how to publish Free Software
--------------------------------------

The AGPL license says, when you convey AGPL software (either verbatim copies
or modified versions), that you need to include the program's source code. But
what's a good way of doing that?

You can read the full text of the AGPL [here][1], but to summarize in plain
English:

* You are allowed to give away exact copies of the source code, as long as you
  keep the license text intact. You are also allowed to charge a price for each
  copy.
* You are allowed to give away modified copies of the source code, as long as
  you state that you changed it, and when you changed it.
* If your software comes as a boxed product, and there's a CD or a DVD in the
  box with the source code on it, then you're OK.
* If your software comes as a boxed product, you can include a note in the box
  with instructions to order a CD/DVD with the source (only shipping costs), or
  a website where people can download the source code. Do either of these and
  you're also OK.
* If your software can be downloaded (free or for a charge), then the source
  code must also be available for download, at no extra cost. The source code
  doesn't have to be included in your software installer, it can be a separate
  download. It doesn't even have to be on the same website, as long as there is
  a clear link to the source code.

That's not all. Most people assume that just the source code used to generate
the software is enough, but actually that also includes any scripts you use to
control the building of your software. General purpose tools don't need to be
included. For example, if you use Maven, your `pom.xml` needs to be there, but
the Java SDK or the Maven software doesn't.

Your install script can even be a text file that simply says, _"copy file A to
directory B, then run command C to compile and then command D to install"_. But
as a software craftsperson, you already use the principles of continuous
delivery, right? So you automate everything as much as possible. Ideally your
end user should be able to run one single command to get from source code to
fully functional software. But still add a short text file, and consider using
the very simple [MarkDown][2] format (`.md`).

What about dependencies? You might require an exact version of a certain SDK.
It may be useful to provide your users with a controlled environment to build
your software. That's where virtualization technology like [Vagrant][3] or 
[Docker][4] can help you. If you include a Vagrant or a Docker configuration
with your source code, then you can be absolutely sure that your user has
exactly the same versions of all development tools as you have. Take a look at
the [iText 7 Core repository on GitHub][5]: you'll see a Vagrant file.

If you have made changes, you need to date those changes. If you are already
following software development best practices, then you are already using a
revision control system like Git, Mercurial or Subversion - Git being the most
used. Every time you commit a change, it's recorded with your name and the date.
So if you publish your Git repository then that's been taken care of. There are
many hosted Git services, the most popular currently are
[GitHub][6] and [Bitbucket][7]. You can even use GitHub as a download hub for
your compiled software when you use release tags!

To summarize:

* Use Git to track your changes.
* Commit your build and install scripts to your repository.
* Consider using a virtualization technology like Vagrant or Docker, and commit
  it's configs to your repository.
* Add a _short_ `README.md` file to explain how to build, install and use your
  software.
* Put your repository on [GitHub][6].
* Add a tag every time you release a new version, and add the compiled software
  to the release tag on GitHub.

Of course, these are just one set of best practices. This is just what works for
us at iText Software. You could even roll it all up in a zip file and host that
on your own website. As long as your end user have easy access to your source
code, you're good.

This blog post was inspired by [Bradley M. Kuhn][8]'s talk _"[A Beautiful Build:
Releasing Linux Source Correctly][9]"_ at [FOSDEM][10] 2016 ([video][11]).

[1]: http://itextpdf.com/AGPL
[2]: https://guides.github.com/features/mastering-markdown/
[3]: https://www.vagrantup.com/
[4]: https://www.docker.com/
[5]: https://github.com/itext/itext7
[6]: http://github.com/
[7]: http://bitbucket.org/
[8]: https://archive.fosdem.org/2016/schedule/speaker/bradley_m_kuhn/
[9]: https://archive.fosdem.org/2016/schedule/event/beautiful_build/
[10]: https://fosdem.org
[11]: http://mirror.onet.pl/pub/mirrors/video.fosdem.org/2016/ud2218a/a-beautiful-build-releasing-linux-source-correctly.mp4