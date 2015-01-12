



<!DOCTYPE html>
<html lang="en" class="">
  <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# object: http://ogp.me/ns/object# article: http://ogp.me/ns/article# profile: http://ogp.me/ns/profile#">
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Language" content="en">
    
    
    <title>xmltree/xmltree.js at master · koppor/xmltree · GitHub</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub">
    <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub">
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-114.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-144.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144.png">
    <meta property="fb:app_id" content="1401488693436528">

      <meta content="@github" name="twitter:site" /><meta content="summary" name="twitter:card" /><meta content="koppor/xmltree" name="twitter:title" /><meta content="xmltree - Visualise and traverse your XML" name="twitter:description" /><meta content="https://avatars1.githubusercontent.com/u/1366654?v=3&amp;s=400" name="twitter:image:src" />
<meta content="GitHub" property="og:site_name" /><meta content="object" property="og:type" /><meta content="https://avatars1.githubusercontent.com/u/1366654?v=3&amp;s=400" property="og:image" /><meta content="koppor/xmltree" property="og:title" /><meta content="https://github.com/koppor/xmltree" property="og:url" /><meta content="xmltree - Visualise and traverse your XML" property="og:description" />

      <meta name="browser-stats-url" content="/_stats">
    <link rel="assets" href="https://assets-cdn.github.com/">
    <link rel="conduit-xhr" href="https://ghconduit.com:25035">
    
    <meta name="pjax-timeout" content="1000">
    

    <meta name="msapplication-TileImage" content="/windows-tile.png">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="selected-link" value="repo_source" data-pjax-transient>
      <meta name="google-analytics" content="UA-3769691-2">

    <meta content="collector.githubapp.com" name="octolytics-host" /><meta content="collector-cdn.github.com" name="octolytics-script-host" /><meta content="github" name="octolytics-app-id" /><meta content="57330788:6D74:10D6B5F:54B39DC5" name="octolytics-dimension-request_id" />
    
    <meta content="Rails, view, blob#show" name="analytics-event" />

    
    
    <link rel="icon" type="image/x-icon" href="https://assets-cdn.github.com/favicon.ico">


    <meta content="authenticity_token" name="csrf-param" />
<meta content="l3+A+2zBxRbF0k709OQLre208h5n045PK/xm/3pkPL9djv1uqkGyMBCzKnxGJUy4TO6ulg6BfjCAw/NS2JbPaw==" name="csrf-token" />

    <link href="https://assets-cdn.github.com/assets/github-ce76938859542000655959cdfe660df23f4b00ca8e645c02e94b82db13f71206.css" media="all" rel="stylesheet" type="text/css" />
    <link href="https://assets-cdn.github.com/assets/github2-24197d40569ab7b3fd7178681236002a61c1b8cd5040223c404813c1fea901a6.css" media="all" rel="stylesheet" type="text/css" />
    
    


    <meta http-equiv="x-pjax-version" content="df46ace3522d1eb59b16d9c11fb78381">

      
  <meta name="description" content="xmltree - Visualise and traverse your XML">
  <meta name="go-import" content="github.com/koppor/xmltree git https://github.com/koppor/xmltree.git">

  <meta content="1366654" name="octolytics-dimension-user_id" /><meta content="koppor" name="octolytics-dimension-user_login" /><meta content="13225270" name="octolytics-dimension-repository_id" /><meta content="koppor/xmltree" name="octolytics-dimension-repository_nwo" /><meta content="true" name="octolytics-dimension-repository_public" /><meta content="false" name="octolytics-dimension-repository_is_fork" /><meta content="13225270" name="octolytics-dimension-repository_network_root_id" /><meta content="koppor/xmltree" name="octolytics-dimension-repository_network_root_nwo" />
  <link href="https://github.com/koppor/xmltree/commits/master.atom" rel="alternate" title="Recent Commits to xmltree:master" type="application/atom+xml">

  </head>


  <body class="logged_out  env-production linux vis-public page-blob">
    <a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>
    <div class="wrapper">
      
      
      
      


      
      <div class="header header-logged-out" role="banner">
  <div class="container clearfix">

    <a class="header-logo-wordmark" href="https://github.com/" ga-data-click="(Logged out) Header, go to homepage, icon:logo-wordmark">
      <span class="mega-octicon octicon-logo-github"></span>
    </a>

    <div class="header-actions" role="navigation">
        <a class="button primary" href="/join" data-ga-click="(Logged out) Header, clicked Sign up, text:sign-up">Sign up</a>
      <a class="button" href="/login?return_to=%2Fkoppor%2Fxmltree%2Fblob%2Fmaster%2Fxmltree.js" data-ga-click="(Logged out) Header, clicked Sign in, text:sign-in">Sign in</a>
    </div>

    <div class="site-search repo-scope js-site-search" role="search">
      <form accept-charset="UTF-8" action="/koppor/xmltree/search" class="js-site-search-form" data-global-search-url="/search" data-repo-search-url="/koppor/xmltree/search" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
  <input type="text"
    class="js-site-search-field is-clearable"
    data-hotkey="s"
    name="q"
    placeholder="Search"
    data-global-scope-placeholder="Search GitHub"
    data-repo-scope-placeholder="Search"
    tabindex="1"
    autocapitalize="off">
  <div class="scope-badge">This repository</div>
</form>
    </div>

      <ul class="header-nav left" role="navigation">
          <li class="header-nav-item">
            <a class="header-nav-link" href="/explore" data-ga-click="(Logged out) Header, go to explore, text:explore">Explore</a>
          </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="/features" data-ga-click="(Logged out) Header, go to features, text:features">Features</a>
          </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="https://enterprise.github.com/" data-ga-click="(Logged out) Header, go to enterprise, text:enterprise">Enterprise</a>
          </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="/blog" data-ga-click="(Logged out) Header, go to blog, text:blog">Blog</a>
          </li>
      </ul>

  </div>
</div>



      <div id="start-of-content" class="accessibility-aid"></div>
          <div class="site" itemscope itemtype="http://schema.org/WebPage">
    <div id="js-flash-container">
      
    </div>
    <div class="pagehead repohead instapaper_ignore readability-menu">
      <div class="container">
        
<ul class="pagehead-actions">


  <li>
      <a href="/login?return_to=%2Fkoppor%2Fxmltree"
    class="minibutton with-count star-button tooltipped tooltipped-n"
    aria-label="You must be signed in to star a repository" rel="nofollow">
    <span class="octicon octicon-star"></span>
    Star
  </a>

    <a class="social-count js-social-count" href="/koppor/xmltree/stargazers">
      3
    </a>

  </li>

    <li>
      <a href="/login?return_to=%2Fkoppor%2Fxmltree"
        class="minibutton with-count js-toggler-target fork-button tooltipped tooltipped-n"
        aria-label="You must be signed in to fork a repository" rel="nofollow">
        <span class="octicon octicon-repo-forked"></span>
        Fork
      </a>
      <a href="/koppor/xmltree/network" class="social-count">
        5
      </a>
    </li>
</ul>

        <h1 itemscope itemtype="http://data-vocabulary.org/Breadcrumb" class="entry-title public">
          <span class="mega-octicon octicon-repo"></span>
          <span class="author"><a href="/koppor" class="url fn" itemprop="url" rel="author"><span itemprop="title">koppor</span></a></span><!--
       --><span class="path-divider">/</span><!--
       --><strong><a href="/koppor/xmltree" class="js-current-repository" data-pjax="#js-repo-pjax-container">xmltree</a></strong>

          <span class="page-context-loader">
            <img alt="" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
          </span>

        </h1>
      </div><!-- /.container -->
    </div><!-- /.repohead -->

    <div class="container">
      <div class="repository-with-sidebar repo-container new-discussion-timeline  ">
        <div class="repository-sidebar clearfix">
            
<nav class="sunken-menu repo-nav js-repo-nav js-sidenav-container-pjax js-octicon-loaders"
     role="navigation"
     data-pjax="#js-repo-pjax-container"
     data-issue-count-url="/koppor/xmltree/issues/counts">
  <ul class="sunken-menu-group">
    <li class="tooltipped tooltipped-w" aria-label="Code">
      <a href="/koppor/xmltree" aria-label="Code" class="selected js-selected-navigation-item sunken-menu-item" data-hotkey="g c" data-selected-links="repo_source repo_downloads repo_commits repo_releases repo_tags repo_branches /koppor/xmltree">
        <span class="octicon octicon-code"></span> <span class="full-word">Code</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>

      <li class="tooltipped tooltipped-w" aria-label="Issues">
        <a href="/koppor/xmltree/issues" aria-label="Issues" class="js-selected-navigation-item sunken-menu-item" data-hotkey="g i" data-selected-links="repo_issues repo_labels repo_milestones /koppor/xmltree/issues">
          <span class="octicon octicon-issue-opened"></span> <span class="full-word">Issues</span>
          <span class="js-issue-replace-counter"></span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>      </li>

    <li class="tooltipped tooltipped-w" aria-label="Pull Requests">
      <a href="/koppor/xmltree/pulls" aria-label="Pull Requests" class="js-selected-navigation-item sunken-menu-item" data-hotkey="g p" data-selected-links="repo_pulls /koppor/xmltree/pulls">
          <span class="octicon octicon-git-pull-request"></span> <span class="full-word">Pull Requests</span>
          <span class="js-pull-replace-counter"></span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>


  </ul>
  <div class="sunken-menu-separator"></div>
  <ul class="sunken-menu-group">

    <li class="tooltipped tooltipped-w" aria-label="Pulse">
      <a href="/koppor/xmltree/pulse" aria-label="Pulse" class="js-selected-navigation-item sunken-menu-item" data-selected-links="pulse /koppor/xmltree/pulse">
        <span class="octicon octicon-pulse"></span> <span class="full-word">Pulse</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>

    <li class="tooltipped tooltipped-w" aria-label="Graphs">
      <a href="/koppor/xmltree/graphs" aria-label="Graphs" class="js-selected-navigation-item sunken-menu-item" data-selected-links="repo_graphs repo_contributors /koppor/xmltree/graphs">
        <span class="octicon octicon-graph"></span> <span class="full-word">Graphs</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>
  </ul>


</nav>

              <div class="only-with-full-nav">
                
  
<div class="clone-url open"
  data-protocol-type="http"
  data-url="/users/set_protocol?protocol_selector=http&amp;protocol_type=clone">
  <h3><span class="text-emphasized">HTTPS</span> clone URL</h3>
  <div class="input-group js-zeroclipboard-container">
    <input type="text" class="input-mini input-monospace js-url-field js-zeroclipboard-target"
           value="https://github.com/koppor/xmltree.git" readonly="readonly">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard minibutton zeroclipboard-button" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>

  
<div class="clone-url "
  data-protocol-type="subversion"
  data-url="/users/set_protocol?protocol_selector=subversion&amp;protocol_type=clone">
  <h3><span class="text-emphasized">Subversion</span> checkout URL</h3>
  <div class="input-group js-zeroclipboard-container">
    <input type="text" class="input-mini input-monospace js-url-field js-zeroclipboard-target"
           value="https://github.com/koppor/xmltree" readonly="readonly">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard minibutton zeroclipboard-button" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>



<p class="clone-options">You can clone with
  <a href="#" class="js-clone-selector" data-protocol="http">HTTPS</a> or <a href="#" class="js-clone-selector" data-protocol="subversion">Subversion</a>.
  <a href="https://help.github.com/articles/which-remote-url-should-i-use" class="help tooltipped tooltipped-n" aria-label="Get help on which URL is right for you.">
    <span class="octicon octicon-question"></span>
  </a>
</p>



                <a href="/koppor/xmltree/archive/master.zip"
                   class="minibutton sidebar-button"
                   aria-label="Download the contents of koppor/xmltree as a zip file"
                   title="Download the contents of koppor/xmltree as a zip file"
                   rel="nofollow">
                  <span class="octicon octicon-cloud-download"></span>
                  Download ZIP
                </a>
              </div>
        </div><!-- /.repository-sidebar -->

        <div id="js-repo-pjax-container" class="repository-content context-loader-container" data-pjax-container>
          

<a href="/koppor/xmltree/blob/dcc9a4a6f449b7aa24015c4661191b157fb0d8fb/xmltree.js" class="hidden js-permalink-shortcut" data-hotkey="y">Permalink</a>

<!-- blob contrib key: blob_contributors:v21:c8d1b838cc940a954ac7fb90dc794559 -->

<div class="file-navigation js-zeroclipboard-container">
  
<div class="select-menu js-menu-container js-select-menu left">
  <span class="minibutton select-menu-button js-menu-target css-truncate" data-hotkey="w"
    data-master-branch="master"
    data-ref="master"
    title="master"
    role="button" aria-label="Switch branches or tags" tabindex="0" aria-haspopup="true">
    <span class="octicon octicon-git-branch"></span>
    <i>branch:</i>
    <span class="js-select-button css-truncate-target">master</span>
  </span>

  <div class="select-menu-modal-holder js-menu-content js-navigation-container" data-pjax aria-hidden="true">

    <div class="select-menu-modal">
      <div class="select-menu-header">
        <span class="select-menu-title">Switch branches/tags</span>
        <span class="octicon octicon-x js-menu-close" role="button" aria-label="Close"></span>
      </div> <!-- /.select-menu-header -->

      <div class="select-menu-filters">
        <div class="select-menu-text-filter">
          <input type="text" aria-label="Filter branches/tags" id="context-commitish-filter-field" class="js-filterable-field js-navigation-enable" placeholder="Filter branches/tags">
        </div>
        <div class="select-menu-tabs">
          <ul>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="branches" class="js-select-menu-tab">Branches</a>
            </li>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="tags" class="js-select-menu-tab">Tags</a>
            </li>
          </ul>
        </div><!-- /.select-menu-tabs -->
      </div><!-- /.select-menu-filters -->

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="branches">

        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/koppor/xmltree/blob/gh-pages/xmltree.js"
                 data-name="gh-pages"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="gh-pages">gh-pages</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item selected">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/koppor/xmltree/blob/master/xmltree.js"
                 data-name="master"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="master">master</a>
            </div> <!-- /.select-menu-item -->
        </div>

          <div class="select-menu-no-results">Nothing to show</div>
      </div> <!-- /.select-menu-list -->

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="tags">
        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/koppor/xmltree/tree/v3.1.2/xmltree.js"
                 data-name="v3.1.2"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v3.1.2">v3.1.2</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/koppor/xmltree/tree/v3.1.1/xmltree.js"
                 data-name="v3.1.1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v3.1.1">v3.1.1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/koppor/xmltree/tree/v3.1.0/xmltree.js"
                 data-name="v3.1.0"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v3.1.0">v3.1.0</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/koppor/xmltree/tree/v3.0.0/xmltree.js"
                 data-name="v3.0.0"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v3.0.0">v3.0.0</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/koppor/xmltree/tree/v2.0.0/xmltree.js"
                 data-name="v2.0.0"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v2.0.0">v2.0.0</a>
            </div> <!-- /.select-menu-item -->
        </div>

        <div class="select-menu-no-results">Nothing to show</div>
      </div> <!-- /.select-menu-list -->

    </div> <!-- /.select-menu-modal -->
  </div> <!-- /.select-menu-modal-holder -->
</div> <!-- /.select-menu -->

  <div class="button-group right">
    <a href="/koppor/xmltree/find/master"
          class="js-show-file-finder minibutton empty-icon tooltipped tooltipped-s"
          data-pjax
          data-hotkey="t"
          aria-label="Quickly jump between files">
      <span class="octicon octicon-list-unordered"></span>
    </a>
    <button aria-label="Copy file path to clipboard" class="js-zeroclipboard minibutton zeroclipboard-button" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
  </div>

  <div class="breadcrumb js-zeroclipboard-target">
    <span class='repo-root js-repo-root'><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/koppor/xmltree" class="" data-branch="master" data-direction="back" data-pjax="true" itemscope="url"><span itemprop="title">xmltree</span></a></span></span><span class="separator">/</span><strong class="final-path">xmltree.js</strong>
  </div>
</div>

<include-fragment class="commit commit-loader file-history-tease" src="/koppor/xmltree/contributors/master/xmltree.js">
  <div class="file-history-tease-header">
    Fetching contributors&hellip;
  </div>

  <div class="participation">
    <p class="loader-loading"><img alt="" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32-EAF2F5.gif" width="16" /></p>
    <p class="loader-error">Cannot retrieve contributors at this time</p>
  </div>
</include-fragment>
<div class="file-box">
  <div class="file">
    <div class="meta clearfix">
      <div class="info file-name">
          <span>469 lines (370 sloc)</span>
          <span class="meta-divider"></span>
        <span>17.994 kb</span>
      </div>
      <div class="actions">
        <div class="button-group">
          <a href="/koppor/xmltree/raw/master/xmltree.js" class="minibutton " id="raw-url">Raw</a>
            <a href="/koppor/xmltree/blame/master/xmltree.js" class="minibutton js-update-url-with-hash">Blame</a>
          <a href="/koppor/xmltree/commits/master/xmltree.js" class="minibutton " rel="nofollow">History</a>
        </div><!-- /.button-group -->


            <a class="octicon-button disabled tooltipped tooltipped-w" href="#"
               aria-label="You must be signed in to make or propose changes"><span class="octicon octicon-pencil"></span></a>

          <a class="octicon-button danger disabled tooltipped tooltipped-w" href="#"
             aria-label="You must be signed in to make or propose changes">
          <span class="octicon octicon-trashcan"></span>
        </a>
      </div><!-- /.actions -->
    </div>
    

  <div class="blob-wrapper data type-javascript">
      <table class="highlight tab-size-8 js-file-line-container">
      <tr>
        <td id="L1" class="blob-num js-line-number" data-line-number="1"></td>
        <td id="LC1" class="blob-code js-file-line"><span class="pl-c">/* ==============</span></td>
      </tr>
      <tr>
        <td id="L2" class="blob-num js-line-number" data-line-number="2"></td>
        <td id="LC2" class="blob-code js-file-line"><span class="pl-c">The MIT License (MIT)</span></td>
      </tr>
      <tr>
        <td id="L3" class="blob-num js-line-number" data-line-number="3"></td>
        <td id="LC3" class="blob-code js-file-line"><span class="pl-c"></span></td>
      </tr>
      <tr>
        <td id="L4" class="blob-num js-line-number" data-line-number="4"></td>
        <td id="LC4" class="blob-code js-file-line"><span class="pl-c">Copyright (c) 2011-2013 Mitya &lt;mitya@mitya.co.uk&gt;</span></td>
      </tr>
      <tr>
        <td id="L5" class="blob-num js-line-number" data-line-number="5"></td>
        <td id="LC5" class="blob-code js-file-line"><span class="pl-c">Copyright (c) 2013 Oliver Kopp</span></td>
      </tr>
      <tr>
        <td id="L6" class="blob-num js-line-number" data-line-number="6"></td>
        <td id="LC6" class="blob-code js-file-line"><span class="pl-c"></span></td>
      </tr>
      <tr>
        <td id="L7" class="blob-num js-line-number" data-line-number="7"></td>
        <td id="LC7" class="blob-code js-file-line"><span class="pl-c">Permission is hereby granted, free of charge, to any person obtaining a copy</span></td>
      </tr>
      <tr>
        <td id="L8" class="blob-num js-line-number" data-line-number="8"></td>
        <td id="LC8" class="blob-code js-file-line"><span class="pl-c">of this software and associated documentation files (the &quot;Software&quot;), to deal</span></td>
      </tr>
      <tr>
        <td id="L9" class="blob-num js-line-number" data-line-number="9"></td>
        <td id="LC9" class="blob-code js-file-line"><span class="pl-c">in the Software without restriction, including without limitation the rights</span></td>
      </tr>
      <tr>
        <td id="L10" class="blob-num js-line-number" data-line-number="10"></td>
        <td id="LC10" class="blob-code js-file-line"><span class="pl-c">to use, copy, modify, merge, publish, distribute, sublicense, and/or sell</span></td>
      </tr>
      <tr>
        <td id="L11" class="blob-num js-line-number" data-line-number="11"></td>
        <td id="LC11" class="blob-code js-file-line"><span class="pl-c">copies of the Software, and to permit persons to whom the Software is</span></td>
      </tr>
      <tr>
        <td id="L12" class="blob-num js-line-number" data-line-number="12"></td>
        <td id="LC12" class="blob-code js-file-line"><span class="pl-c">furnished to do so, subject to the following conditions:</span></td>
      </tr>
      <tr>
        <td id="L13" class="blob-num js-line-number" data-line-number="13"></td>
        <td id="LC13" class="blob-code js-file-line"><span class="pl-c"></span></td>
      </tr>
      <tr>
        <td id="L14" class="blob-num js-line-number" data-line-number="14"></td>
        <td id="LC14" class="blob-code js-file-line"><span class="pl-c">The above copyright notice and this permission notice shall be included in</span></td>
      </tr>
      <tr>
        <td id="L15" class="blob-num js-line-number" data-line-number="15"></td>
        <td id="LC15" class="blob-code js-file-line"><span class="pl-c">all copies or substantial portions of the Software.</span></td>
      </tr>
      <tr>
        <td id="L16" class="blob-num js-line-number" data-line-number="16"></td>
        <td id="LC16" class="blob-code js-file-line"><span class="pl-c"></span></td>
      </tr>
      <tr>
        <td id="L17" class="blob-num js-line-number" data-line-number="17"></td>
        <td id="LC17" class="blob-code js-file-line"><span class="pl-c">THE SOFTWARE IS PROVIDED &quot;AS IS&quot;, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR</span></td>
      </tr>
      <tr>
        <td id="L18" class="blob-num js-line-number" data-line-number="18"></td>
        <td id="LC18" class="blob-code js-file-line"><span class="pl-c">IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,</span></td>
      </tr>
      <tr>
        <td id="L19" class="blob-num js-line-number" data-line-number="19"></td>
        <td id="LC19" class="blob-code js-file-line"><span class="pl-c">FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE</span></td>
      </tr>
      <tr>
        <td id="L20" class="blob-num js-line-number" data-line-number="20"></td>
        <td id="LC20" class="blob-code js-file-line"><span class="pl-c">AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER</span></td>
      </tr>
      <tr>
        <td id="L21" class="blob-num js-line-number" data-line-number="21"></td>
        <td id="LC21" class="blob-code js-file-line"><span class="pl-c">LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,</span></td>
      </tr>
      <tr>
        <td id="L22" class="blob-num js-line-number" data-line-number="22"></td>
        <td id="LC22" class="blob-code js-file-line"><span class="pl-c">OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN</span></td>
      </tr>
      <tr>
        <td id="L23" class="blob-num js-line-number" data-line-number="23"></td>
        <td id="LC23" class="blob-code js-file-line"><span class="pl-c">THE SOFTWARE.</span></td>
      </tr>
      <tr>
        <td id="L24" class="blob-num js-line-number" data-line-number="24"></td>
        <td id="LC24" class="blob-code js-file-line"><span class="pl-c"></span></td>
      </tr>
      <tr>
        <td id="L25" class="blob-num js-line-number" data-line-number="25"></td>
        <td id="LC25" class="blob-code js-file-line"><span class="pl-c">| @author: Mitya &lt;mitya@mitya.co.uk&gt;</span></td>
      </tr>
      <tr>
        <td id="L26" class="blob-num js-line-number" data-line-number="26"></td>
        <td id="LC26" class="blob-code js-file-line"><span class="pl-c">| @Docs &amp; demo: http://www.mitya.co.uk/scripts/XML-Tree---visualise-and-traverse-your-XML-186</span></td>
      </tr>
      <tr>
        <td id="L27" class="blob-num js-line-number" data-line-number="27"></td>
        <td id="LC27" class="blob-code js-file-line"><span class="pl-c">|</span></td>
      </tr>
      <tr>
        <td id="L28" class="blob-num js-line-number" data-line-number="28"></td>
        <td id="LC28" class="blob-code js-file-line"><span class="pl-c">| github page: http://www.github.com/koppor/xmltree</span></td>
      </tr>
      <tr>
        <td id="L29" class="blob-num js-line-number" data-line-number="29"></td>
        <td id="LC29" class="blob-code js-file-line"><span class="pl-c">============== */</span></td>
      </tr>
      <tr>
        <td id="L30" class="blob-num js-line-number" data-line-number="30"></td>
        <td id="LC30" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L31" class="blob-num js-line-number" data-line-number="31"></td>
        <td id="LC31" class="blob-code js-file-line"><span class="pl-c">// AMD and non-AMD compatibility inspired by http://tkareine.org/blog/2012/08/11/why-javascript-needs-module-definitions/ and https://github.com/blueimp/jQuery-File-Upload/blob/9.5.0/js/jquery.fileupload.js</span></td>
      </tr>
      <tr>
        <td id="L32" class="blob-num js-line-number" data-line-number="32"></td>
        <td id="LC32" class="blob-code js-file-line">(<span class="pl-st">function</span>(<span class="pl-vpf">factory</span>) {</td>
      </tr>
      <tr>
        <td id="L33" class="blob-num js-line-number" data-line-number="33"></td>
        <td id="LC33" class="blob-code js-file-line">  <span class="pl-k">if</span> (<span class="pl-k">typeof</span> define <span class="pl-k">===</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>function<span class="pl-pds">&#39;</span></span> <span class="pl-k">&amp;&amp;</span> define.amd) {</td>
      </tr>
      <tr>
        <td id="L34" class="blob-num js-line-number" data-line-number="34"></td>
        <td id="LC34" class="blob-code js-file-line">    <span class="pl-c">// Register as named AMD module. Anonymous registering causes &quot;Mismatched anonymous define() module&quot; in requirejs 2.9.1 when script is loaded explicitly and then loaded with requirejs</span></td>
      </tr>
      <tr>
        <td id="L35" class="blob-num js-line-number" data-line-number="35"></td>
        <td id="LC35" class="blob-code js-file-line">    define(<span class="pl-s1"><span class="pl-pds">&quot;</span>xmltree<span class="pl-pds">&quot;</span></span>, [<span class="pl-s1"><span class="pl-pds">&#39;</span>jquery<span class="pl-pds">&#39;</span></span>], factory);</td>
      </tr>
      <tr>
        <td id="L36" class="blob-num js-line-number" data-line-number="36"></td>
        <td id="LC36" class="blob-code js-file-line">  } <span class="pl-k">else</span> {</td>
      </tr>
      <tr>
        <td id="L37" class="blob-num js-line-number" data-line-number="37"></td>
        <td id="LC37" class="blob-code js-file-line">    <span class="pl-c">// traditional browser loading: build object with directly referencing the global jQuery object and inject it into window object</span></td>
      </tr>
      <tr>
        <td id="L38" class="blob-num js-line-number" data-line-number="38"></td>
        <td id="LC38" class="blob-code js-file-line">    <span class="pl-c">// &quot;XMLTree&quot; is used to provide backwards compatiblity with XMLTree 3.0.0</span></td>
      </tr>
      <tr>
        <td id="L39" class="blob-num js-line-number" data-line-number="39"></td>
        <td id="LC39" class="blob-code js-file-line">    <span class="pl-s3">window</span>.XMLTree <span class="pl-k">=</span> factory(<span class="pl-s3">window</span>.jQuery);</td>
      </tr>
      <tr>
        <td id="L40" class="blob-num js-line-number" data-line-number="40"></td>
        <td id="LC40" class="blob-code js-file-line">  }</td>
      </tr>
      <tr>
        <td id="L41" class="blob-num js-line-number" data-line-number="41"></td>
        <td id="LC41" class="blob-code js-file-line">}(<span class="pl-st">function</span>(<span class="pl-vpf">$</span>) {</td>
      </tr>
      <tr>
        <td id="L42" class="blob-num js-line-number" data-line-number="42"></td>
        <td id="LC42" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L43" class="blob-num js-line-number" data-line-number="43"></td>
        <td id="LC43" class="blob-code js-file-line">	<span class="pl-c">//log progress?</span></td>
      </tr>
      <tr>
        <td id="L44" class="blob-num js-line-number" data-line-number="44"></td>
        <td id="LC44" class="blob-code js-file-line">	<span class="pl-s">var</span> log <span class="pl-k">=</span> location.<span class="pl-sc">href</span>.<span class="pl-s3">indexOf</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>XMLTreeLog=true<span class="pl-pds">&#39;</span></span>) <span class="pl-k">!=</span> <span class="pl-k">-</span><span class="pl-c1">1</span>;</td>
      </tr>
      <tr>
        <td id="L45" class="blob-num js-line-number" data-line-number="45"></td>
        <td id="LC45" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L46" class="blob-num js-line-number" data-line-number="46"></td>
        <td id="LC46" class="blob-code js-file-line">	<span class="pl-en">XMLTree</span> <span class="pl-k">=</span> <span class="pl-st">function</span>(<span class="pl-vpf">jdo</span>, <span class="pl-vpf">subTreeRequest</span>) {</td>
      </tr>
      <tr>
        <td id="L47" class="blob-num js-line-number" data-line-number="47"></td>
        <td id="LC47" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L48" class="blob-num js-line-number" data-line-number="48"></td>
        <td id="LC48" class="blob-code js-file-line">		<span class="pl-c">/* -------------------</span></td>
      </tr>
      <tr>
        <td id="L49" class="blob-num js-line-number" data-line-number="49"></td>
        <td id="LC49" class="blob-code js-file-line"><span class="pl-c">		| PREP &amp; VALIDATION</span></td>
      </tr>
      <tr>
        <td id="L50" class="blob-num js-line-number" data-line-number="50"></td>
        <td id="LC50" class="blob-code js-file-line"><span class="pl-c">		------------------- */</span></td>
      </tr>
      <tr>
        <td id="L51" class="blob-num js-line-number" data-line-number="51"></td>
        <td id="LC51" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L52" class="blob-num js-line-number" data-line-number="52"></td>
        <td id="LC52" class="blob-code js-file-line">		<span class="pl-k">if</span> (location.<span class="pl-sc">href</span>.<span class="pl-s3">indexOf</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>debug<span class="pl-pds">&#39;</span></span>) <span class="pl-k">!=</span> <span class="pl-k">-</span><span class="pl-c1">1</span>) <span class="pl-v">this</span>.debug <span class="pl-k">=</span> <span class="pl-c1">true</span>;</td>
      </tr>
      <tr>
        <td id="L53" class="blob-num js-line-number" data-line-number="53"></td>
        <td id="LC53" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L54" class="blob-num js-line-number" data-line-number="54"></td>
        <td id="LC54" class="blob-code js-file-line">		<span class="pl-c">//ensure was instantiated, not merely called</span></td>
      </tr>
      <tr>
        <td id="L55" class="blob-num js-line-number" data-line-number="55"></td>
        <td id="LC55" class="blob-code js-file-line">		<span class="pl-k">if</span> (<span class="pl-k">!</span>(<span class="pl-v">this</span> <span class="pl-k">instanceof</span> XMLTree)) {</td>
      </tr>
      <tr>
        <td id="L56" class="blob-num js-line-number" data-line-number="56"></td>
        <td id="LC56" class="blob-code js-file-line">			debug(<span class="pl-s1"><span class="pl-pds">&quot;</span>XMLTree was called but not instantiated<span class="pl-pds">&quot;</span></span>);</td>
      </tr>
      <tr>
        <td id="L57" class="blob-num js-line-number" data-line-number="57"></td>
        <td id="LC57" class="blob-code js-file-line">			<span class="pl-k">return</span>;</td>
      </tr>
      <tr>
        <td id="L58" class="blob-num js-line-number" data-line-number="58"></td>
        <td id="LC58" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L59" class="blob-num js-line-number" data-line-number="59"></td>
        <td id="LC59" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L60" class="blob-num js-line-number" data-line-number="60"></td>
        <td id="LC60" class="blob-code js-file-line">		<span class="pl-c">//validate some params</span></td>
      </tr>
      <tr>
        <td id="L61" class="blob-num js-line-number" data-line-number="61"></td>
        <td id="LC61" class="blob-code js-file-line">		<span class="pl-s">var</span> error;</td>
      </tr>
      <tr>
        <td id="L62" class="blob-num js-line-number" data-line-number="62"></td>
        <td id="LC62" class="blob-code js-file-line">		<span class="pl-k">if</span> (<span class="pl-k">!</span>jdo.fpath <span class="pl-k">&amp;&amp;</span> <span class="pl-k">!</span>jdo.xml) error <span class="pl-k">=</span> <span class="pl-s1"><span class="pl-pds">&quot;</span>neither XML nor path to XML file passed<span class="pl-pds">&quot;</span></span>;</td>
      </tr>
      <tr>
        <td id="L63" class="blob-num js-line-number" data-line-number="63"></td>
        <td id="LC63" class="blob-code js-file-line">		<span class="pl-k">else</span> <span class="pl-k">if</span> ((<span class="pl-k">!</span>jdo.container <span class="pl-k">||</span> <span class="pl-k">!</span>$(jdo.container).<span class="pl-sc">length</span>) <span class="pl-k">&amp;&amp;</span> <span class="pl-k">!</span>jdo.justReturn) error <span class="pl-k">=</span> <span class="pl-s1"><span class="pl-pds">&quot;</span>No container selector passed or does not match element in DOM<span class="pl-pds">&quot;</span></span>;</td>
      </tr>
      <tr>
        <td id="L64" class="blob-num js-line-number" data-line-number="64"></td>
        <td id="LC64" class="blob-code js-file-line">		<span class="pl-k">if</span> (error) { <span class="pl-s3">alert</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>XMLTree error - <span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>error); <span class="pl-k">return</span>; }</td>
      </tr>
      <tr>
        <td id="L65" class="blob-num js-line-number" data-line-number="65"></td>
        <td id="LC65" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L66" class="blob-num js-line-number" data-line-number="66"></td>
        <td id="LC66" class="blob-code js-file-line">		<span class="pl-c">//some vars</span></td>
      </tr>
      <tr>
        <td id="L67" class="blob-num js-line-number" data-line-number="67"></td>
        <td id="LC67" class="blob-code js-file-line">		<span class="pl-s">var</span>	li,</td>
      </tr>
      <tr>
        <td id="L68" class="blob-num js-line-number" data-line-number="68"></td>
        <td id="LC68" class="blob-code js-file-line">			appendTo,</td>
      </tr>
      <tr>
        <td id="L69" class="blob-num js-line-number" data-line-number="69"></td>
        <td id="LC69" class="blob-code js-file-line">			attrLI,</td>
      </tr>
      <tr>
        <td id="L70" class="blob-num js-line-number" data-line-number="70"></td>
        <td id="LC70" class="blob-code js-file-line">			container <span class="pl-k">=</span> $(jdo.container),</td>
      </tr>
      <tr>
        <td id="L71" class="blob-num js-line-number" data-line-number="71"></td>
        <td id="LC71" class="blob-code js-file-line">			thiss <span class="pl-k">=</span> <span class="pl-v">this</span>,</td>
      </tr>
      <tr>
        <td id="L72" class="blob-num js-line-number" data-line-number="72"></td>
        <td id="LC72" class="blob-code js-file-line">			fpath_req;</td>
      </tr>
      <tr>
        <td id="L73" class="blob-num js-line-number" data-line-number="73"></td>
        <td id="LC73" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L74" class="blob-num js-line-number" data-line-number="74"></td>
        <td id="LC74" class="blob-code js-file-line">		<span class="pl-c">//establish tree container - if making the outer tree, create a new UL. If this is a sub-tree request, i.e. called by self,</span></td>
      </tr>
      <tr>
        <td id="L75" class="blob-num js-line-number" data-line-number="75"></td>
        <td id="LC75" class="blob-code js-file-line">		<span class="pl-c">//merge new tree into existing UL of caller LI</span></td>
      </tr>
      <tr>
        <td id="L76" class="blob-num js-line-number" data-line-number="76"></td>
        <td id="LC76" class="blob-code js-file-line">		<span class="pl-v">this</span>.tree <span class="pl-k">=</span> <span class="pl-k">!</span>subTreeRequest <span class="pl-k">?</span> $(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;ul&gt;<span class="pl-pds">&#39;</span></span>) <span class="pl-k">:</span> container.children(<span class="pl-s1"><span class="pl-pds">&#39;</span>ul<span class="pl-pds">&#39;</span></span>).hide();</td>
      </tr>
      <tr>
        <td id="L77" class="blob-num js-line-number" data-line-number="77"></td>
        <td id="LC77" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L78" class="blob-num js-line-number" data-line-number="78"></td>
        <td id="LC78" class="blob-code js-file-line">		<span class="pl-c">//log this instance of the tree and update global instances tracker</span></td>
      </tr>
      <tr>
        <td id="L79" class="blob-num js-line-number" data-line-number="79"></td>
        <td id="LC79" class="blob-code js-file-line">		<span class="pl-v">this</span>.instanceID <span class="pl-k">=</span> XMLTree.instancesCounter;</td>
      </tr>
      <tr>
        <td id="L80" class="blob-num js-line-number" data-line-number="80"></td>
        <td id="LC80" class="blob-code js-file-line">		<span class="pl-v">this</span>.tree.attr(<span class="pl-s1"><span class="pl-pds">&#39;</span>id<span class="pl-pds">&#39;</span></span>, <span class="pl-s1"><span class="pl-pds">&#39;</span>tree_<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span><span class="pl-v">this</span>.instanceID);</td>
      </tr>
      <tr>
        <td id="L81" class="blob-num js-line-number" data-line-number="81"></td>
        <td id="LC81" class="blob-code js-file-line">		XMLTree.instancesCounter<span class="pl-k">++</span>;</td>
      </tr>
      <tr>
        <td id="L82" class="blob-num js-line-number" data-line-number="82"></td>
        <td id="LC82" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L83" class="blob-num js-line-number" data-line-number="83"></td>
        <td id="LC83" class="blob-code js-file-line">		<span class="pl-c">//add a few classes to tree, unless it&#39;s a sub-tree (i.e. being inserted later into branch of master tree, in which case it can</span></td>
      </tr>
      <tr>
        <td id="L84" class="blob-num js-line-number" data-line-number="84"></td>
        <td id="LC84" class="blob-code js-file-line">		<span class="pl-c">//just inherit master tree&#39;s classes</span></td>
      </tr>
      <tr>
        <td id="L85" class="blob-num js-line-number" data-line-number="85"></td>
        <td id="LC85" class="blob-code js-file-line">		<span class="pl-k">if</span> (<span class="pl-k">!</span>subTreeRequest) {</td>
      </tr>
      <tr>
        <td id="L86" class="blob-num js-line-number" data-line-number="86"></td>
        <td id="LC86" class="blob-code js-file-line">			<span class="pl-v">this</span>.tree.addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>xmltree<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L87" class="blob-num js-line-number" data-line-number="87"></td>
        <td id="LC87" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo[<span class="pl-s1"><span class="pl-pds">&#39;</span>class<span class="pl-pds">&#39;</span></span>]) <span class="pl-v">this</span>.tree.addClass(jdo[<span class="pl-s1"><span class="pl-pds">&#39;</span>class<span class="pl-pds">&#39;</span></span>]);</td>
      </tr>
      <tr>
        <td id="L88" class="blob-num js-line-number" data-line-number="88"></td>
        <td id="LC88" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.startExpanded) <span class="pl-v">this</span>.tree.addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>startExpanded<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L89" class="blob-num js-line-number" data-line-number="89"></td>
        <td id="LC89" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L90" class="blob-num js-line-number" data-line-number="90"></td>
        <td id="LC90" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L91" class="blob-num js-line-number" data-line-number="91"></td>
        <td id="LC91" class="blob-code js-file-line">		<span class="pl-c">//and any data?</span></td>
      </tr>
      <tr>
        <td id="L92" class="blob-num js-line-number" data-line-number="92"></td>
        <td id="LC92" class="blob-code js-file-line">		<span class="pl-k">if</span> (jdo.<span class="pl-sc">data</span>) <span class="pl-k">for</span> (<span class="pl-s">var</span> i <span class="pl-k">in</span> jdo.<span class="pl-sc">data</span>) <span class="pl-v">this</span>.tree.<span class="pl-sc">data</span>(i, jdo.<span class="pl-sc">data</span>[i]);</td>
      </tr>
      <tr>
        <td id="L93" class="blob-num js-line-number" data-line-number="93"></td>
        <td id="LC93" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L94" class="blob-num js-line-number" data-line-number="94"></td>
        <td id="LC94" class="blob-code js-file-line">		<span class="pl-c">//if it is a sub-tree request, add .forcePlusMin to tree (i.e. expanded LI) so plus/min icon of sub-tree shows, doesn&#39;t inherit</span></td>
      </tr>
      <tr>
        <td id="L95" class="blob-num js-line-number" data-line-number="95"></td>
        <td id="LC95" class="blob-code js-file-line">		<span class="pl-c">//CSS from parent to hide it</span></td>
      </tr>
      <tr>
        <td id="L96" class="blob-num js-line-number" data-line-number="96"></td>
        <td id="LC96" class="blob-code js-file-line">		<span class="pl-k">if</span> (subTreeRequest) <span class="pl-v">this</span>.tree.addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>forcePlusMin<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L97" class="blob-num js-line-number" data-line-number="97"></td>
        <td id="LC97" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L98" class="blob-num js-line-number" data-line-number="98"></td>
        <td id="LC98" class="blob-code js-file-line">		<span class="pl-c">//insert master UL, unless just returning tree, not inserting it</span></td>
      </tr>
      <tr>
        <td id="L99" class="blob-num js-line-number" data-line-number="99"></td>
        <td id="LC99" class="blob-code js-file-line">		<span class="pl-k">if</span> (<span class="pl-k">!</span>jdo.justReturn) <span class="pl-v">this</span>.tree.appendTo(container);</td>
      </tr>
      <tr>
        <td id="L100" class="blob-num js-line-number" data-line-number="100"></td>
        <td id="LC100" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L101" class="blob-num js-line-number" data-line-number="101"></td>
        <td id="LC101" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L102" class="blob-num js-line-number" data-line-number="102"></td>
        <td id="LC102" class="blob-code js-file-line">		<span class="pl-c">/* -------------------</span></td>
      </tr>
      <tr>
        <td id="L103" class="blob-num js-line-number" data-line-number="103"></td>
        <td id="LC103" class="blob-code js-file-line"><span class="pl-c">		| ESTABLISH XML - either from file or passed manually. If latter, temporarily rename all tags so any sharing names of self-</span></td>
      </tr>
      <tr>
        <td id="L104" class="blob-num js-line-number" data-line-number="104"></td>
        <td id="LC104" class="blob-code js-file-line"><span class="pl-c">		| closing HTML tags aren&#39;t mullered by jQuery during delving</span></td>
      </tr>
      <tr>
        <td id="L105" class="blob-num js-line-number" data-line-number="105"></td>
        <td id="LC105" class="blob-code js-file-line"><span class="pl-c">		------------------- */</span></td>
      </tr>
      <tr>
        <td id="L106" class="blob-num js-line-number" data-line-number="106"></td>
        <td id="LC106" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L107" class="blob-num js-line-number" data-line-number="107"></td>
        <td id="LC107" class="blob-code js-file-line">		<span class="pl-c">//get XML from file (&#39;done&#39; handler fires not here but slightly further down)</span></td>
      </tr>
      <tr>
        <td id="L108" class="blob-num js-line-number" data-line-number="108"></td>
        <td id="LC108" class="blob-code js-file-line">		<span class="pl-k">if</span> (jdo.fpath) {</td>
      </tr>
      <tr>
        <td id="L109" class="blob-num js-line-number" data-line-number="109"></td>
        <td id="LC109" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L110" class="blob-num js-line-number" data-line-number="110"></td>
        <td id="LC110" class="blob-code js-file-line">			debug(<span class="pl-s1"><span class="pl-pds">&#39;</span>XML tree fpath:<span class="pl-pds">&#39;</span></span>, jdo.fpath);</td>
      </tr>
      <tr>
        <td id="L111" class="blob-num js-line-number" data-line-number="111"></td>
        <td id="LC111" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L112" class="blob-num js-line-number" data-line-number="112"></td>
        <td id="LC112" class="blob-code js-file-line">			<span class="pl-c">//get data...</span></td>
      </tr>
      <tr>
        <td id="L113" class="blob-num js-line-number" data-line-number="113"></td>
        <td id="LC113" class="blob-code js-file-line">			<span class="pl-s">var</span> dataType <span class="pl-k">=</span> <span class="pl-k">!</span>jdo.jsonp <span class="pl-k">?</span> (<span class="pl-k">!</span>jdo.json <span class="pl-k">?</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>xml<span class="pl-pds">&#39;</span></span> <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>json<span class="pl-pds">&#39;</span></span>) <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>jsonp<span class="pl-pds">&#39;</span></span>;</td>
      </tr>
      <tr>
        <td id="L114" class="blob-num js-line-number" data-line-number="114"></td>
        <td id="LC114" class="blob-code js-file-line">			fpath_req <span class="pl-k">=</span> $.ajax({url<span class="pl-k">:</span> jdo.fpath, cache<span class="pl-k">:</span> jdo.cache <span class="pl-k">==</span> <span class="pl-c1">undefined</span> <span class="pl-k">?</span> true <span class="pl-k">:</span> jdo.cache, dataType<span class="pl-k">:</span> dataType})</td>
      </tr>
      <tr>
        <td id="L115" class="blob-num js-line-number" data-line-number="115"></td>
        <td id="LC115" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L116" class="blob-num js-line-number" data-line-number="116"></td>
        <td id="LC116" class="blob-code js-file-line">				<span class="pl-c">//...success. Establish XML. If jdo.json, convert JSON respone to XML text then reinitialise</span></td>
      </tr>
      <tr>
        <td id="L117" class="blob-num js-line-number" data-line-number="117"></td>
        <td id="LC117" class="blob-code js-file-line">				.done(<span class="pl-st">function</span>(<span class="pl-vpf">data</span>) {</td>
      </tr>
      <tr>
        <td id="L118" class="blob-num js-line-number" data-line-number="118"></td>
        <td id="LC118" class="blob-code js-file-line">					<span class="pl-k">if</span> (jdo.json) {</td>
      </tr>
      <tr>
        <td id="L119" class="blob-num js-line-number" data-line-number="119"></td>
        <td id="LC119" class="blob-code js-file-line">						<span class="pl-k">if</span> (jdo.jsonCallback) data <span class="pl-k">=</span> jdo.jsonCallback(data);</td>
      </tr>
      <tr>
        <td id="L120" class="blob-num js-line-number" data-line-number="120"></td>
        <td id="LC120" class="blob-code js-file-line">						<span class="pl-k">delete</span> jdo.fpath;</td>
      </tr>
      <tr>
        <td id="L121" class="blob-num js-line-number" data-line-number="121"></td>
        <td id="LC121" class="blob-code js-file-line">						jdo.xml <span class="pl-k">=</span> json_to_xml(data);</td>
      </tr>
      <tr>
        <td id="L122" class="blob-num js-line-number" data-line-number="122"></td>
        <td id="LC122" class="blob-code js-file-line">						<span class="pl-k">return</span> <span class="pl-k">new</span> <span class="pl-en">XMLTree</span>(jdo, subTreeRequest);</td>
      </tr>
      <tr>
        <td id="L123" class="blob-num js-line-number" data-line-number="123"></td>
        <td id="LC123" class="blob-code js-file-line">					} <span class="pl-k">else</span> {</td>
      </tr>
      <tr>
        <td id="L124" class="blob-num js-line-number" data-line-number="124"></td>
        <td id="LC124" class="blob-code js-file-line">						data <span class="pl-k">=</span> cleanXML(data);</td>
      </tr>
      <tr>
        <td id="L125" class="blob-num js-line-number" data-line-number="125"></td>
        <td id="LC125" class="blob-code js-file-line">					}</td>
      </tr>
      <tr>
        <td id="L126" class="blob-num js-line-number" data-line-number="126"></td>
        <td id="LC126" class="blob-code js-file-line">					thiss.xml <span class="pl-k">=</span> data;</td>
      </tr>
      <tr>
        <td id="L127" class="blob-num js-line-number" data-line-number="127"></td>
        <td id="LC127" class="blob-code js-file-line">					actOnXML.<span class="pl-s3">call</span>(thiss, data);</td>
      </tr>
      <tr>
        <td id="L128" class="blob-num js-line-number" data-line-number="128"></td>
        <td id="LC128" class="blob-code js-file-line">				})</td>
      </tr>
      <tr>
        <td id="L129" class="blob-num js-line-number" data-line-number="129"></td>
        <td id="LC129" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L130" class="blob-num js-line-number" data-line-number="130"></td>
        <td id="LC130" class="blob-code js-file-line">				<span class="pl-c">//...error</span></td>
      </tr>
      <tr>
        <td id="L131" class="blob-num js-line-number" data-line-number="131"></td>
        <td id="LC131" class="blob-code js-file-line">				<span class="pl-s3">.error</span>(<span class="pl-st">function</span>() { <span class="pl-s3">alert</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>XMLTree error - could not load XML from <span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>jdo.fpath); });</td>
      </tr>
      <tr>
        <td id="L132" class="blob-num js-line-number" data-line-number="132"></td>
        <td id="LC132" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L133" class="blob-num js-line-number" data-line-number="133"></td>
        <td id="LC133" class="blob-code js-file-line">		<span class="pl-c">//passed as string</span></td>
      </tr>
      <tr>
        <td id="L134" class="blob-num js-line-number" data-line-number="134"></td>
        <td id="LC134" class="blob-code js-file-line">		} <span class="pl-k">else</span> {</td>
      </tr>
      <tr>
        <td id="L135" class="blob-num js-line-number" data-line-number="135"></td>
        <td id="LC135" class="blob-code js-file-line">			<span class="pl-k">if</span> (<span class="pl-k">typeof</span> jdo.xml <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>string<span class="pl-pds">&#39;</span></span>) {</td>
      </tr>
      <tr>
        <td id="L136" class="blob-num js-line-number" data-line-number="136"></td>
        <td id="LC136" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L137" class="blob-num js-line-number" data-line-number="137"></td>
        <td id="LC137" class="blob-code js-file-line">				<span class="pl-v">this</span>.xml <span class="pl-k">=</span> cleanXML(jdo.xml);</td>
      </tr>
      <tr>
        <td id="L138" class="blob-num js-line-number" data-line-number="138"></td>
        <td id="LC138" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L139" class="blob-num js-line-number" data-line-number="139"></td>
        <td id="LC139" class="blob-code js-file-line">				<span class="pl-c">//also strip out entities as they break JS XML parsing</span></td>
      </tr>
      <tr>
        <td id="L140" class="blob-num js-line-number" data-line-number="140"></td>
        <td id="LC140" class="blob-code js-file-line">				<span class="pl-c">//this.xml = this.xml.replace(/&amp;amp;|&amp;(?= )/g, &#39;and&#39;).replace(/&amp;\w+;/g, &#39;&#39;);</span></td>
      </tr>
      <tr>
        <td id="L141" class="blob-num js-line-number" data-line-number="141"></td>
        <td id="LC141" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L142" class="blob-num js-line-number" data-line-number="142"></td>
        <td id="LC142" class="blob-code js-file-line">				actOnXML.<span class="pl-s3">call</span>(<span class="pl-v">this</span>);</td>
      </tr>
      <tr>
        <td id="L143" class="blob-num js-line-number" data-line-number="143"></td>
        <td id="LC143" class="blob-code js-file-line">			} <span class="pl-k">else</span> {</td>
      </tr>
      <tr>
        <td id="L144" class="blob-num js-line-number" data-line-number="144"></td>
        <td id="LC144" class="blob-code js-file-line">				<span class="pl-s3">alert</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>XMLTree error - jdo.xml is not a string<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L145" class="blob-num js-line-number" data-line-number="145"></td>
        <td id="LC145" class="blob-code js-file-line">			}</td>
      </tr>
      <tr>
        <td id="L146" class="blob-num js-line-number" data-line-number="146"></td>
        <td id="LC146" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L147" class="blob-num js-line-number" data-line-number="147"></td>
        <td id="LC147" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L148" class="blob-num js-line-number" data-line-number="148"></td>
        <td id="LC148" class="blob-code js-file-line">		<span class="pl-c">/**</span></td>
      </tr>
      <tr>
        <td id="L149" class="blob-num js-line-number" data-line-number="149"></td>
        <td id="LC149" class="blob-code js-file-line"><span class="pl-c">		 * Does some XML cleaning</span></td>
      </tr>
      <tr>
        <td id="L150" class="blob-num js-line-number" data-line-number="150"></td>
        <td id="LC150" class="blob-code js-file-line"><span class="pl-c">		 */</span></td>
      </tr>
      <tr>
        <td id="L151" class="blob-num js-line-number" data-line-number="151"></td>
        <td id="LC151" class="blob-code js-file-line">		<span class="pl-st">function</span> <span class="pl-en">cleanXML</span>(<span class="pl-vpf">xml</span>) {</td>
      </tr>
      <tr>
        <td id="L152" class="blob-num js-line-number" data-line-number="152"></td>
        <td id="LC152" class="blob-code js-file-line">			<span class="pl-c">// just strip xml processing instruction</span></td>
      </tr>
      <tr>
        <td id="L153" class="blob-num js-line-number" data-line-number="153"></td>
        <td id="LC153" class="blob-code js-file-line">			<span class="pl-k">return</span> xml.<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span>&lt;<span class="pl-cce">\?</span>xml<span class="pl-c1">[<span class="pl-k">^</span>&gt;]</span><span class="pl-k">+</span>&gt;<span class="pl-c1">\s</span><span class="pl-k">*</span><span class="pl-pds">/</span></span>, <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L154" class="blob-num js-line-number" data-line-number="154"></td>
        <td id="LC154" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L155" class="blob-num js-line-number" data-line-number="155"></td>
        <td id="LC155" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L156" class="blob-num js-line-number" data-line-number="156"></td>
        <td id="LC156" class="blob-code js-file-line">		<span class="pl-c">/* -------------------</span></td>
      </tr>
      <tr>
        <td id="L157" class="blob-num js-line-number" data-line-number="157"></td>
        <td id="LC157" class="blob-code js-file-line"><span class="pl-c">		| ACT ON XML - once we have the XML, start outputting from it. If XML is string, first parse.</span></td>
      </tr>
      <tr>
        <td id="L158" class="blob-num js-line-number" data-line-number="158"></td>
        <td id="LC158" class="blob-code js-file-line"><span class="pl-c">		------------------- */</span></td>
      </tr>
      <tr>
        <td id="L159" class="blob-num js-line-number" data-line-number="159"></td>
        <td id="LC159" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L160" class="blob-num js-line-number" data-line-number="160"></td>
        <td id="LC160" class="blob-code js-file-line">		<span class="pl-st">function</span> <span class="pl-en">actOnXML</span>() {</td>
      </tr>
      <tr>
        <td id="L161" class="blob-num js-line-number" data-line-number="161"></td>
        <td id="LC161" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L162" class="blob-num js-line-number" data-line-number="162"></td>
        <td id="LC162" class="blob-code js-file-line">			<span class="pl-s">var</span> thiss <span class="pl-k">=</span> <span class="pl-v">this</span>;</td>
      </tr>
      <tr>
        <td id="L163" class="blob-num js-line-number" data-line-number="163"></td>
        <td id="LC163" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L164" class="blob-num js-line-number" data-line-number="164"></td>
        <td id="LC164" class="blob-code js-file-line">			<span class="pl-c">//establish XML (parsing as required) as a jQuery object</span></td>
      </tr>
      <tr>
        <td id="L165" class="blob-num js-line-number" data-line-number="165"></td>
        <td id="LC165" class="blob-code js-file-line">			<span class="pl-v">this</span>.xml <span class="pl-k">=</span> $(<span class="pl-k">typeof</span> <span class="pl-v">this</span>.xml <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>string<span class="pl-pds">&#39;</span></span> <span class="pl-k">?</span> parseXML(<span class="pl-v">this</span>.xml) <span class="pl-k">:</span> <span class="pl-v">this</span>.xml);</td>
      </tr>
      <tr>
        <td id="L166" class="blob-num js-line-number" data-line-number="166"></td>
        <td id="LC166" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L167" class="blob-num js-line-number" data-line-number="167"></td>
        <td id="LC167" class="blob-code js-file-line">			<span class="pl-c">//if is sub-tree request, we don&#39;t want the root, just the items</span></td>
      </tr>
      <tr>
        <td id="L168" class="blob-num js-line-number" data-line-number="168"></td>
        <td id="LC168" class="blob-code js-file-line">			<span class="pl-k">if</span> (subTreeRequest) <span class="pl-v">this</span>.xml <span class="pl-k">=</span> <span class="pl-v">this</span>.xml.children(<span class="pl-s1"><span class="pl-pds">&#39;</span>:first<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L169" class="blob-num js-line-number" data-line-number="169"></td>
        <td id="LC169" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L170" class="blob-num js-line-number" data-line-number="170"></td>
        <td id="LC170" class="blob-code js-file-line">			<span class="pl-c">//perform any XML manipulation rules stipulated</span></td>
      </tr>
      <tr>
        <td id="L171" class="blob-num js-line-number" data-line-number="171"></td>
        <td id="LC171" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.XMLCallback) <span class="pl-v">this</span>.xml <span class="pl-k">=</span> jdo.XMLCallback(<span class="pl-v">this</span>.xml);</td>
      </tr>
      <tr>
        <td id="L172" class="blob-num js-line-number" data-line-number="172"></td>
        <td id="LC172" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L173" class="blob-num js-line-number" data-line-number="173"></td>
        <td id="LC173" class="blob-code js-file-line">			debug(<span class="pl-s1"><span class="pl-pds">&#39;</span>XML fed to XMLTree:<span class="pl-pds">&#39;</span></span>, <span class="pl-v">this</span>.xml);</td>
      </tr>
      <tr>
        <td id="L174" class="blob-num js-line-number" data-line-number="174"></td>
        <td id="LC174" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L175" class="blob-num js-line-number" data-line-number="175"></td>
        <td id="LC175" class="blob-code js-file-line">			<span class="pl-c">//open the tree at a specific point once output? Log as attribute on the XML node, so later we can spot this and</span></td>
      </tr>
      <tr>
        <td id="L176" class="blob-num js-line-number" data-line-number="176"></td>
        <td id="LC176" class="blob-code js-file-line">			<span class="pl-c">//open from that point</span></td>
      </tr>
      <tr>
        <td id="L177" class="blob-num js-line-number" data-line-number="177"></td>
        <td id="LC177" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.openAtPath) { <span class="pl-s">var</span> currSel <span class="pl-k">=</span> <span class="pl-v">this</span>.xml.<span class="pl-s3">find</span>(jdo.openAtPath); <span class="pl-k">if</span> (currSel.<span class="pl-sc">length</span> <span class="pl-k">==</span> <span class="pl-c1">1</span>) currSel.attr(<span class="pl-s1"><span class="pl-pds">&#39;</span>currSel<span class="pl-pds">&#39;</span></span>, <span class="pl-s1"><span class="pl-pds">&#39;</span>true<span class="pl-pds">&#39;</span></span>); }</td>
      </tr>
      <tr>
        <td id="L178" class="blob-num js-line-number" data-line-number="178"></td>
        <td id="LC178" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L179" class="blob-num js-line-number" data-line-number="179"></td>
        <td id="LC179" class="blob-code js-file-line">			<span class="pl-c">//start delving. Since JS seems to add another, outer root element, our (real) root it is child</span></td>
      </tr>
      <tr>
        <td id="L180" class="blob-num js-line-number" data-line-number="180"></td>
        <td id="LC180" class="blob-code js-file-line">			<span class="pl-v">this</span>.xml.children().each(<span class="pl-st">function</span>() { delve($(<span class="pl-v">this</span>), thiss.tree); });</td>
      </tr>
      <tr>
        <td id="L181" class="blob-num js-line-number" data-line-number="181"></td>
        <td id="LC181" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L182" class="blob-num js-line-number" data-line-number="182"></td>
        <td id="LC182" class="blob-code js-file-line">			<span class="pl-c">//if sub-tree, if we ended up with no data, remove tree and also corresponding plus/min. Else show tree.</span></td>
      </tr>
      <tr>
        <td id="L183" class="blob-num js-line-number" data-line-number="183"></td>
        <td id="LC183" class="blob-code js-file-line">			<span class="pl-k">if</span> (subTreeRequest) <span class="pl-v">this</span>.xml.children().<span class="pl-sc">length</span> <span class="pl-k">?</span> <span class="pl-v">this</span>.tree.show() <span class="pl-k">:</span> <span class="pl-v">this</span>.tree.prev(<span class="pl-s1"><span class="pl-pds">&#39;</span>.plusMin<span class="pl-pds">&#39;</span></span>).andSelf().<span class="pl-s3">remove</span>();</td>
      </tr>
      <tr>
        <td id="L184" class="blob-num js-line-number" data-line-number="184"></td>
        <td id="LC184" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L185" class="blob-num js-line-number" data-line-number="185"></td>
        <td id="LC185" class="blob-code js-file-line">			<span class="pl-c">//do post-build stuff after delving complete</span></td>
      </tr>
      <tr>
        <td id="L186" class="blob-num js-line-number" data-line-number="186"></td>
        <td id="LC186" class="blob-code js-file-line">			postBuild.<span class="pl-s3">call</span>(<span class="pl-v">this</span>);</td>
      </tr>
      <tr>
        <td id="L187" class="blob-num js-line-number" data-line-number="187"></td>
        <td id="LC187" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L188" class="blob-num js-line-number" data-line-number="188"></td>
        <td id="LC188" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L189" class="blob-num js-line-number" data-line-number="189"></td>
        <td id="LC189" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L190" class="blob-num js-line-number" data-line-number="190"></td>
        <td id="LC190" class="blob-code js-file-line">		<span class="pl-c">/* -------------------</span></td>
      </tr>
      <tr>
        <td id="L191" class="blob-num js-line-number" data-line-number="191"></td>
        <td id="LC191" class="blob-code js-file-line"><span class="pl-c">		| MAIN FUNC for outputting. Called recursively for all levels of tree</span></td>
      </tr>
      <tr>
        <td id="L192" class="blob-num js-line-number" data-line-number="192"></td>
        <td id="LC192" class="blob-code js-file-line"><span class="pl-c">		------------------- */</span></td>
      </tr>
      <tr>
        <td id="L193" class="blob-num js-line-number" data-line-number="193"></td>
        <td id="LC193" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L194" class="blob-num js-line-number" data-line-number="194"></td>
        <td id="LC194" class="blob-code js-file-line">		<span class="pl-st">function</span> <span class="pl-en">delve</span>(<span class="pl-vpf">node</span>, <span class="pl-vpf">appendTo</span>) {</td>
      </tr>
      <tr>
        <td id="L195" class="blob-num js-line-number" data-line-number="195"></td>
        <td id="LC195" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L196" class="blob-num js-line-number" data-line-number="196"></td>
        <td id="LC196" class="blob-code js-file-line">			<span class="pl-s">var</span> tagName, li, ul, attrs, i, kids, storedProcedure, LITxtHolder;</td>
      </tr>
      <tr>
        <td id="L197" class="blob-num js-line-number" data-line-number="197"></td>
        <td id="LC197" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L198" class="blob-num js-line-number" data-line-number="198"></td>
        <td id="LC198" class="blob-code js-file-line">			<span class="pl-c">//what&#39;s this node&#39;s tag name?</span></td>
      </tr>
      <tr>
        <td id="L199" class="blob-num js-line-number" data-line-number="199"></td>
        <td id="LC199" class="blob-code js-file-line">			tagName <span class="pl-k">=</span> node[<span class="pl-c1">0</span>].<span class="pl-sc">tagName</span>;</td>
      </tr>
      <tr>
        <td id="L200" class="blob-num js-line-number" data-line-number="200"></td>
        <td id="LC200" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L201" class="blob-num js-line-number" data-line-number="201"></td>
        <td id="LC201" class="blob-code js-file-line">			<span class="pl-c">//build LI and sub-UL for this node (note, tagname is applied as class to LI, for easy post-tree traversal)</span></td>
      </tr>
      <tr>
        <td id="L202" class="blob-num js-line-number" data-line-number="202"></td>
        <td id="LC202" class="blob-code js-file-line">			appendTo.append((li <span class="pl-k">=</span> $(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;li&gt;<span class="pl-pds">&#39;</span></span>).addClass(tagName).append(LITxtHolder <span class="pl-k">=</span> $(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;span&gt;<span class="pl-pds">&#39;</span></span>).addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>LIText<span class="pl-pds">&#39;</span></span>)).append(ul <span class="pl-k">=</span> $(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;ul&gt;<span class="pl-pds">&#39;</span></span>))));</td>
      </tr>
      <tr>
        <td id="L203" class="blob-num js-line-number" data-line-number="203"></td>
        <td id="LC203" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L204" class="blob-num js-line-number" data-line-number="204"></td>
        <td id="LC204" class="blob-code js-file-line">			<span class="pl-c">//plus/mins indicator</span></td>
      </tr>
      <tr>
        <td id="L205" class="blob-num js-line-number" data-line-number="205"></td>
        <td id="LC205" class="blob-code js-file-line">			li.append($(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;span&gt;<span class="pl-pds">&#39;</span></span>, {html<span class="pl-k">:</span> jdo.startExpanded <span class="pl-k">?</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>-<span class="pl-pds">&#39;</span></span> <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>+<span class="pl-pds">&#39;</span></span>}).addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>plusMin expanded<span class="pl-pds">&#39;</span></span>));</td>
      </tr>
      <tr>
        <td id="L206" class="blob-num js-line-number" data-line-number="206"></td>
        <td id="LC206" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L207" class="blob-num js-line-number" data-line-number="207"></td>
        <td id="LC207" class="blob-code js-file-line">			<span class="pl-c">//attributes...</span></td>
      </tr>
      <tr>
        <td id="L208" class="blob-num js-line-number" data-line-number="208"></td>
        <td id="LC208" class="blob-code js-file-line">			attrs <span class="pl-k">=</span> node[<span class="pl-c1">0</span>].<span class="pl-sc">attributes</span>;</td>
      </tr>
      <tr>
        <td id="L209" class="blob-num js-line-number" data-line-number="209"></td>
        <td id="LC209" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L210" class="blob-num js-line-number" data-line-number="210"></td>
        <td id="LC210" class="blob-code js-file-line">			<span class="pl-c">//...add node attributes as classes? If true, all, else if array, only attributes specified in that array</span></td>
      </tr>
      <tr>
        <td id="L211" class="blob-num js-line-number" data-line-number="211"></td>
        <td id="LC211" class="blob-code js-file-line">			<span class="pl-c">//For each eligible attribute, two classes are added: attr and attr-value</span></td>
      </tr>
      <tr>
        <td id="L212" class="blob-num js-line-number" data-line-number="212"></td>
        <td id="LC212" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.attrsAsClasses) {</td>
      </tr>
      <tr>
        <td id="L213" class="blob-num js-line-number" data-line-number="213"></td>
        <td id="LC213" class="blob-code js-file-line">				<span class="pl-k">for</span> (i<span class="pl-k">=</span><span class="pl-c1">0</span>; i<span class="pl-k">&lt;</span>attrs.<span class="pl-sc">length</span>; i<span class="pl-k">++</span>)</td>
      </tr>
      <tr>
        <td id="L214" class="blob-num js-line-number" data-line-number="214"></td>
        <td id="LC214" class="blob-code js-file-line">					<span class="pl-k">if</span> (jdo.attrsAsClasses <span class="pl-k">===</span> <span class="pl-c1">true</span> <span class="pl-k">||</span> (<span class="pl-k">typeof</span> jdo.attrsAsClasses <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>string<span class="pl-pds">&#39;</span></span> <span class="pl-k">&amp;&amp;</span> jdo.attrsAsClasses <span class="pl-k">==</span> attrs[i].<span class="pl-sc">name</span>) <span class="pl-k">||</span> (jdo.attrsAsClasses <span class="pl-k">instanceof</span> <span class="pl-s3">Array</span> <span class="pl-k">&amp;&amp;</span> $.inArray(attrs[i].<span class="pl-sc">name</span>, jdo.attrsAsClasses) <span class="pl-k">!=</span> <span class="pl-k">-</span><span class="pl-c1">1</span>))</td>
      </tr>
      <tr>
        <td id="L215" class="blob-num js-line-number" data-line-number="215"></td>
        <td id="LC215" class="blob-code js-file-line">						li.addClass(attrs[i].<span class="pl-sc">name</span><span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>-<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>attrs[i].<span class="pl-sc">value</span><span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span> <span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>attrs[i].<span class="pl-sc">name</span>);</td>
      </tr>
      <tr>
        <td id="L216" class="blob-num js-line-number" data-line-number="216"></td>
        <td id="LC216" class="blob-code js-file-line">			}</td>
      </tr>
      <tr>
        <td id="L217" class="blob-num js-line-number" data-line-number="217"></td>
        <td id="LC217" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L218" class="blob-num js-line-number" data-line-number="218"></td>
        <td id="LC218" class="blob-code js-file-line">			<span class="pl-c">//...add node attributes as element data? &quot; &quot; &quot; &quot; &quot; &quot; &quot;</span></td>
      </tr>
      <tr>
        <td id="L219" class="blob-num js-line-number" data-line-number="219"></td>
        <td id="LC219" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.attrsAsData) {</td>
      </tr>
      <tr>
        <td id="L220" class="blob-num js-line-number" data-line-number="220"></td>
        <td id="LC220" class="blob-code js-file-line">				<span class="pl-k">for</span> (<span class="pl-s">var</span> i<span class="pl-k">=</span><span class="pl-c1">0</span>; i<span class="pl-k">&lt;</span>attrs.<span class="pl-sc">length</span>; i<span class="pl-k">++</span>)</td>
      </tr>
      <tr>
        <td id="L221" class="blob-num js-line-number" data-line-number="221"></td>
        <td id="LC221" class="blob-code js-file-line">					<span class="pl-k">if</span> (jdo.attrsAsData <span class="pl-k">===</span> <span class="pl-c1">true</span> <span class="pl-k">||</span> (<span class="pl-k">typeof</span> jdo.attrsAsData <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>string<span class="pl-pds">&#39;</span></span> <span class="pl-k">&amp;&amp;</span> jdo.attrsAsData <span class="pl-k">==</span> attrs[i].<span class="pl-sc">name</span>) <span class="pl-k">||</span> (jdo.attrsAsData <span class="pl-k">instanceof</span> <span class="pl-s3">Array</span> <span class="pl-k">&amp;&amp;</span> $.inArray(attrs[i].<span class="pl-sc">name</span>, jdo.attrsAsData) <span class="pl-k">!=</span> <span class="pl-k">-</span><span class="pl-c1">1</span>))</td>
      </tr>
      <tr>
        <td id="L222" class="blob-num js-line-number" data-line-number="222"></td>
        <td id="LC222" class="blob-code js-file-line">						li.attr(<span class="pl-s1"><span class="pl-pds">&#39;</span>data-<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>attrs[i].<span class="pl-sc">name</span>, attrs[i].<span class="pl-sc">value</span>);</td>
      </tr>
      <tr>
        <td id="L223" class="blob-num js-line-number" data-line-number="223"></td>
        <td id="LC223" class="blob-code js-file-line">			}</td>
      </tr>
      <tr>
        <td id="L224" class="blob-num js-line-number" data-line-number="224"></td>
        <td id="LC224" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L225" class="blob-num js-line-number" data-line-number="225"></td>
        <td id="LC225" class="blob-code js-file-line">			<span class="pl-c">//...output attributes as LIs? (yes, no, or yes but hidden)</span></td>
      </tr>
      <tr>
        <td id="L226" class="blob-num js-line-number" data-line-number="226"></td>
        <td id="LC226" class="blob-code js-file-line">			<span class="pl-k">if</span> (<span class="pl-k">!</span>jdo.attrs <span class="pl-k">||</span> jdo.attrs <span class="pl-k">!=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>ignore<span class="pl-pds">&#39;</span></span>) {</td>
      </tr>
      <tr>
        <td id="L227" class="blob-num js-line-number" data-line-number="227"></td>
        <td id="LC227" class="blob-code js-file-line">				<span class="pl-k">if</span> (attrs) {</td>
      </tr>
      <tr>
        <td id="L228" class="blob-num js-line-number" data-line-number="228"></td>
        <td id="LC228" class="blob-code js-file-line">					<span class="pl-k">for</span>(i<span class="pl-k">=</span><span class="pl-c1">0</span>; i<span class="pl-k">&lt;</span>attrs.<span class="pl-sc">length</span>; i<span class="pl-k">++</span>) {</td>
      </tr>
      <tr>
        <td id="L229" class="blob-num js-line-number" data-line-number="229"></td>
        <td id="LC229" class="blob-code js-file-line">						<span class="pl-k">if</span> (attrs[i].<span class="pl-sc">value</span>) {</td>
      </tr>
      <tr>
        <td id="L230" class="blob-num js-line-number" data-line-number="230"></td>
        <td id="LC230" class="blob-code js-file-line">							ul.append(attrLI <span class="pl-k">=</span> $(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;li&gt;<span class="pl-pds">&#39;</span></span>).append($(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;span&gt;<span class="pl-pds">&#39;</span></span>, {text<span class="pl-k">:</span> attrs[i].<span class="pl-sc">value</span>}).addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>attrValue<span class="pl-pds">&#39;</span></span>)).addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>attr <span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>attrs[i].<span class="pl-sc">name</span>).prepend($(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;span&gt;<span class="pl-pds">&#39;</span></span>, {text<span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>@<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>attrs[i].<span class="pl-sc">name</span><span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>:<span class="pl-pds">&#39;</span></span>})));</td>
      </tr>
      <tr>
        <td id="L231" class="blob-num js-line-number" data-line-number="231"></td>
        <td id="LC231" class="blob-code js-file-line">							<span class="pl-k">if</span> (jdo.attrs <span class="pl-k">&amp;&amp;</span> jdo.attrs <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>hidden<span class="pl-pds">&#39;</span></span>) attrLI.hide();</td>
      </tr>
      <tr>
        <td id="L232" class="blob-num js-line-number" data-line-number="232"></td>
        <td id="LC232" class="blob-code js-file-line">						}</td>
      </tr>
      <tr>
        <td id="L233" class="blob-num js-line-number" data-line-number="233"></td>
        <td id="LC233" class="blob-code js-file-line">					}</td>
      </tr>
      <tr>
        <td id="L234" class="blob-num js-line-number" data-line-number="234"></td>
        <td id="LC234" class="blob-code js-file-line">				}</td>
      </tr>
      <tr>
        <td id="L235" class="blob-num js-line-number" data-line-number="235"></td>
        <td id="LC235" class="blob-code js-file-line">			} <span class="pl-k">else</span></td>
      </tr>
      <tr>
        <td id="L236" class="blob-num js-line-number" data-line-number="236"></td>
        <td id="LC236" class="blob-code js-file-line">				attrs <span class="pl-k">=</span> <span class="pl-c1">false</span>;</td>
      </tr>
      <tr>
        <td id="L237" class="blob-num js-line-number" data-line-number="237"></td>
        <td id="LC237" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L238" class="blob-num js-line-number" data-line-number="238"></td>
        <td id="LC238" class="blob-code js-file-line">			<span class="pl-c">//node has children? (for current purposes, attributes are considered children). If contains only attributes, and jdo.attrs</span></td>
      </tr>
      <tr>
        <td id="L239" class="blob-num js-line-number" data-line-number="239"></td>
        <td id="LC239" class="blob-code js-file-line">			<span class="pl-c">//== &#39;hidden&#39;, count as having no kids</span></td>
      </tr>
      <tr>
        <td id="L240" class="blob-num js-line-number" data-line-number="240"></td>
        <td id="LC240" class="blob-code js-file-line">			kids <span class="pl-k">=</span> node.children();</td>
      </tr>
      <tr>
        <td id="L241" class="blob-num js-line-number" data-line-number="241"></td>
        <td id="LC241" class="blob-code js-file-line">			<span class="pl-k">if</span> (<span class="pl-k">!</span>kids.<span class="pl-sc">length</span> <span class="pl-k">&amp;&amp;</span> (<span class="pl-k">!</span>attrs.<span class="pl-sc">length</span> <span class="pl-k">||</span> (attrs.<span class="pl-sc">length</span> <span class="pl-k">&amp;&amp;</span> jdo.attrs <span class="pl-k">&amp;&amp;</span> jdo.attrs <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>hidden<span class="pl-pds">&#39;</span></span>))) li.addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>noKids<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L242" class="blob-num js-line-number" data-line-number="242"></td>
        <td id="LC242" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L243" class="blob-num js-line-number" data-line-number="243"></td>
        <td id="LC243" class="blob-code js-file-line">			<span class="pl-c">//span to show node name</span></td>
      </tr>
      <tr>
        <td id="L244" class="blob-num js-line-number" data-line-number="244"></td>
        <td id="LC244" class="blob-code js-file-line">			tagName <span class="pl-k">=</span> $(<span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;span&gt;<span class="pl-pds">&#39;</span></span>, {text<span class="pl-k">:</span> tagName}).addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>tree_node<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L245" class="blob-num js-line-number" data-line-number="245"></td>
        <td id="LC245" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L246" class="blob-num js-line-number" data-line-number="246"></td>
        <td id="LC246" class="blob-code js-file-line">			<span class="pl-c">//if no children, simply append text (if any)</span></td>
      </tr>
      <tr>
        <td id="L247" class="blob-num js-line-number" data-line-number="247"></td>
        <td id="LC247" class="blob-code js-file-line">			<span class="pl-k">if</span> (<span class="pl-k">!</span>kids.<span class="pl-sc">length</span>)</td>
      </tr>
      <tr>
        <td id="L248" class="blob-num js-line-number" data-line-number="248"></td>
        <td id="LC248" class="blob-code js-file-line">				LITxtHolder.prepend(node.immediateText()).prepend(tagName);</td>
      </tr>
      <tr>
        <td id="L249" class="blob-num js-line-number" data-line-number="249"></td>
        <td id="LC249" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L250" class="blob-num js-line-number" data-line-number="250"></td>
        <td id="LC250" class="blob-code js-file-line">			<span class="pl-c">//if children, set stored procedures that will run and create them only when branch expanded - unless starting expanded</span></td>
      </tr>
      <tr>
        <td id="L251" class="blob-num js-line-number" data-line-number="251"></td>
        <td id="LC251" class="blob-code js-file-line">			<span class="pl-c">//or if tree involves sub-trees</span></td>
      </tr>
      <tr>
        <td id="L252" class="blob-num js-line-number" data-line-number="252"></td>
        <td id="LC252" class="blob-code js-file-line">			<span class="pl-k">else</span> {</td>
      </tr>
      <tr>
        <td id="L253" class="blob-num js-line-number" data-line-number="253"></td>
        <td id="LC253" class="blob-code js-file-line">				LITxtHolder.prepend(node.immediateText()<span class="pl-k">+</span>(<span class="pl-k">!</span>jdo.noDots <span class="pl-k">?</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>..<span class="pl-pds">&#39;</span></span> <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-pds">&#39;</span></span>)).prepend(tagName);</td>
      </tr>
      <tr>
        <td id="L254" class="blob-num js-line-number" data-line-number="254"></td>
        <td id="LC254" class="blob-code js-file-line">				storedProcedure <span class="pl-k">=</span> (<span class="pl-st">function</span>(<span class="pl-vpf">kids</span>, <span class="pl-vpf">parent</span>) { <span class="pl-k">return</span> <span class="pl-st">function</span>() {</td>
      </tr>
      <tr>
        <td id="L255" class="blob-num js-line-number" data-line-number="255"></td>
        <td id="LC255" class="blob-code js-file-line">					kids.each(<span class="pl-st">function</span>() { delve($(<span class="pl-v">this</span>), parent); });</td>
      </tr>
      <tr>
        <td id="L256" class="blob-num js-line-number" data-line-number="256"></td>
        <td id="LC256" class="blob-code js-file-line">					<span class="pl-k">if</span> (jdo.renderCallback) jdo.renderCallback(parent, <span class="pl-v">this</span>, subTreeRequest);</td>
      </tr>
      <tr>
        <td id="L257" class="blob-num js-line-number" data-line-number="257"></td>
        <td id="LC257" class="blob-code js-file-line">				}; })(kids, ul);</td>
      </tr>
      <tr>
        <td id="L258" class="blob-num js-line-number" data-line-number="258"></td>
        <td id="LC258" class="blob-code js-file-line">				<span class="pl-k">if</span> (<span class="pl-k">!</span>jdo.startExpanded <span class="pl-k">&amp;&amp;</span> <span class="pl-k">!</span>jdo.subTreeBranches) {</td>
      </tr>
      <tr>
        <td id="L259" class="blob-num js-line-number" data-line-number="259"></td>
        <td id="LC259" class="blob-code js-file-line">					li.children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.plusMin<span class="pl-pds">&#39;</span></span>).bind(<span class="pl-s1"><span class="pl-pds">&#39;</span>click.sp<span class="pl-pds">&#39;</span></span>, <span class="pl-st">function</span>() {</td>
      </tr>
      <tr>
        <td id="L260" class="blob-num js-line-number" data-line-number="260"></td>
        <td id="LC260" class="blob-code js-file-line">						storedProcedure();</td>
      </tr>
      <tr>
        <td id="L261" class="blob-num js-line-number" data-line-number="261"></td>
        <td id="LC261" class="blob-code js-file-line">						$(<span class="pl-v">this</span>).unbind(<span class="pl-s1"><span class="pl-pds">&#39;</span>click.sp<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L262" class="blob-num js-line-number" data-line-number="262"></td>
        <td id="LC262" class="blob-code js-file-line">					});</td>
      </tr>
      <tr>
        <td id="L263" class="blob-num js-line-number" data-line-number="263"></td>
        <td id="LC263" class="blob-code js-file-line">				} <span class="pl-k">else</span></td>
      </tr>
      <tr>
        <td id="L264" class="blob-num js-line-number" data-line-number="264"></td>
        <td id="LC264" class="blob-code js-file-line">					storedProcedure();</td>
      </tr>
      <tr>
        <td id="L265" class="blob-num js-line-number" data-line-number="265"></td>
        <td id="LC265" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L266" class="blob-num js-line-number" data-line-number="266"></td>
        <td id="LC266" class="blob-code js-file-line">			}</td>
      </tr>
      <tr>
        <td id="L267" class="blob-num js-line-number" data-line-number="267"></td>
        <td id="LC267" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L268" class="blob-num js-line-number" data-line-number="268"></td>
        <td id="LC268" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L269" class="blob-num js-line-number" data-line-number="269"></td>
        <td id="LC269" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L270" class="blob-num js-line-number" data-line-number="270"></td>
        <td id="LC270" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L271" class="blob-num js-line-number" data-line-number="271"></td>
        <td id="LC271" class="blob-code js-file-line">		<span class="pl-c">/* -------------------</span></td>
      </tr>
      <tr>
        <td id="L272" class="blob-num js-line-number" data-line-number="272"></td>
        <td id="LC272" class="blob-code js-file-line"><span class="pl-c">		| POST BUILD stuff, e.g. click events, any user-defined HTML rules, update hash log in URL etc</span></td>
      </tr>
      <tr>
        <td id="L273" class="blob-num js-line-number" data-line-number="273"></td>
        <td id="LC273" class="blob-code js-file-line"><span class="pl-c">		------------------- */</span></td>
      </tr>
      <tr>
        <td id="L274" class="blob-num js-line-number" data-line-number="274"></td>
        <td id="LC274" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L275" class="blob-num js-line-number" data-line-number="275"></td>
        <td id="LC275" class="blob-code js-file-line">		<span class="pl-st">function</span> <span class="pl-en">postBuild</span>() {</td>
      </tr>
      <tr>
        <td id="L276" class="blob-num js-line-number" data-line-number="276"></td>
        <td id="LC276" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L277" class="blob-num js-line-number" data-line-number="277"></td>
        <td id="LC277" class="blob-code js-file-line">			<span class="pl-c">//if doing sub-tree requests, ensure relevent branches always have plus-min icons visible</span></td>
      </tr>
      <tr>
        <td id="L278" class="blob-num js-line-number" data-line-number="278"></td>
        <td id="LC278" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.subTreeBranches) {</td>
      </tr>
      <tr>
        <td id="L279" class="blob-num js-line-number" data-line-number="279"></td>
        <td id="LC279" class="blob-code js-file-line">				<span class="pl-k">if</span> (jdo.subTreeBranches <span class="pl-k">===</span> <span class="pl-c1">true</span>)</td>
      </tr>
      <tr>
        <td id="L280" class="blob-num js-line-number" data-line-number="280"></td>
        <td id="LC280" class="blob-code js-file-line">					<span class="pl-v">this</span>.tree.addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>subTreeRequestsOnAllNodes<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L281" class="blob-num js-line-number" data-line-number="281"></td>
        <td id="LC281" class="blob-code js-file-line">				<span class="pl-k">else</span></td>
      </tr>
      <tr>
        <td id="L282" class="blob-num js-line-number" data-line-number="282"></td>
        <td id="LC282" class="blob-code js-file-line">					<span class="pl-v">this</span>.tree.<span class="pl-s3">find</span>(jdo.subTreeBranches).addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>subTreeNode<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L283" class="blob-num js-line-number" data-line-number="283"></td>
        <td id="LC283" class="blob-code js-file-line">			}</td>
      </tr>
      <tr>
        <td id="L284" class="blob-num js-line-number" data-line-number="284"></td>
        <td id="LC284" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L285" class="blob-num js-line-number" data-line-number="285"></td>
        <td id="LC285" class="blob-code js-file-line">			<span class="pl-c">//listen for clicks to expand/collapse nodes.</span></td>
      </tr>
      <tr>
        <td id="L286" class="blob-num js-line-number" data-line-number="286"></td>
        <td id="LC286" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L287" class="blob-num js-line-number" data-line-number="287"></td>
        <td id="LC287" class="blob-code js-file-line">			<span class="pl-v">this</span>.tree.on(<span class="pl-s1"><span class="pl-pds">&#39;</span>click<span class="pl-pds">&#39;</span></span>, <span class="pl-s1"><span class="pl-pds">&#39;</span>.plusMin<span class="pl-pds">&#39;</span></span>, <span class="pl-st">function</span>(<span class="pl-vpf">evt</span>) {</td>
      </tr>
      <tr>
        <td id="L288" class="blob-num js-line-number" data-line-number="288"></td>
        <td id="LC288" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L289" class="blob-num js-line-number" data-line-number="289"></td>
        <td id="LC289" class="blob-code js-file-line">				<span class="pl-c">//prep</span></td>
      </tr>
      <tr>
        <td id="L290" class="blob-num js-line-number" data-line-number="290"></td>
        <td id="LC290" class="blob-code js-file-line">				evt.stopPropagation();</td>
      </tr>
      <tr>
        <td id="L291" class="blob-num js-line-number" data-line-number="291"></td>
        <td id="LC291" class="blob-code js-file-line">				<span class="pl-s">var</span></td>
      </tr>
      <tr>
        <td id="L292" class="blob-num js-line-number" data-line-number="292"></td>
        <td id="LC292" class="blob-code js-file-line">				uls <span class="pl-k">=</span> $(<span class="pl-v">this</span>).<span class="pl-sc">parent</span>().children(<span class="pl-s1"><span class="pl-pds">&#39;</span>ul<span class="pl-pds">&#39;</span></span>),</td>
      </tr>
      <tr>
        <td id="L293" class="blob-num js-line-number" data-line-number="293"></td>
        <td id="LC293" class="blob-code js-file-line">				currState <span class="pl-k">=</span> $(<span class="pl-v">this</span>).is(<span class="pl-s1"><span class="pl-pds">&#39;</span>.collapsed<span class="pl-pds">&#39;</span></span>) <span class="pl-k">?</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>closed<span class="pl-pds">&#39;</span></span> <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>open<span class="pl-pds">&#39;</span></span>,</td>
      </tr>
      <tr>
        <td id="L294" class="blob-num js-line-number" data-line-number="294"></td>
        <td id="LC294" class="blob-code js-file-line">				xPathToNode <span class="pl-k">=</span> returnXPathToNode($(<span class="pl-v">this</span>).<span class="pl-sc">parent</span>()),</td>
      </tr>
      <tr>
        <td id="L295" class="blob-num js-line-number" data-line-number="295"></td>
        <td id="LC295" class="blob-code js-file-line">				li <span class="pl-k">=</span> $(<span class="pl-v">this</span>).<span class="pl-sc">parent</span>();</td>
      </tr>
      <tr>
        <td id="L296" class="blob-num js-line-number" data-line-number="296"></td>
        <td id="LC296" class="blob-code js-file-line">				uls[currState <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>closed<span class="pl-pds">&#39;</span></span> <span class="pl-k">?</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>show<span class="pl-pds">&#39;</span></span> <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>hide<span class="pl-pds">&#39;</span></span>]();</td>
      </tr>
      <tr>
        <td id="L297" class="blob-num js-line-number" data-line-number="297"></td>
        <td id="LC297" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L298" class="blob-num js-line-number" data-line-number="298"></td>
        <td id="LC298" class="blob-code js-file-line">				<span class="pl-c">//Plus/min click callback? Pass LI, LI&#39;s XPath, event obj. and string &#39;open&#39; or &#39;close&#39;</span></td>
      </tr>
      <tr>
        <td id="L299" class="blob-num js-line-number" data-line-number="299"></td>
        <td id="LC299" class="blob-code js-file-line">				<span class="pl-k">if</span> (jdo.plusMinCallback) jdo.plusMinCallback(li, xPathToNode, evt, currState);</td>
      </tr>
      <tr>
        <td id="L300" class="blob-num js-line-number" data-line-number="300"></td>
        <td id="LC300" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L301" class="blob-num js-line-number" data-line-number="301"></td>
        <td id="LC301" class="blob-code js-file-line">				<span class="pl-c">//Sub-tree request on expand? This should be a callback that returns a request URI that will load a sub-tree into</span></td>
      </tr>
      <tr>
        <td id="L302" class="blob-num js-line-number" data-line-number="302"></td>
        <td id="LC302" class="blob-code js-file-line">				<span class="pl-c">//the current branch. Callback receives same args as plusMinCallback above. If data previously fetched (denoted</span></td>
      </tr>
      <tr>
        <td id="L303" class="blob-num js-line-number" data-line-number="303"></td>
        <td id="LC303" class="blob-code js-file-line">				<span class="pl-c">//by data element on node), ignore.</span></td>
      </tr>
      <tr>
        <td id="L304" class="blob-num js-line-number" data-line-number="304"></td>
        <td id="LC304" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L305" class="blob-num js-line-number" data-line-number="305"></td>
        <td id="LC305" class="blob-code js-file-line">				<span class="pl-k">if</span> (jdo.subTreeBranches <span class="pl-k">&amp;&amp;</span> (jdo.subTreeBranches <span class="pl-k">===</span> <span class="pl-c1">true</span> <span class="pl-k">||</span> $(<span class="pl-v">this</span>).<span class="pl-sc">parent</span>().is(<span class="pl-s1"><span class="pl-pds">&#39;</span>.subTreeNode<span class="pl-pds">&#39;</span></span>)) <span class="pl-k">&amp;&amp;</span> jdo.subTreeRequest <span class="pl-k">&amp;&amp;</span> currState <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>closed<span class="pl-pds">&#39;</span></span> <span class="pl-k">&amp;&amp;</span> <span class="pl-k">!</span>li.<span class="pl-sc">data</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>subTreeDataFetched<span class="pl-pds">&#39;</span></span>)) {</td>
      </tr>
      <tr>
        <td id="L306" class="blob-num js-line-number" data-line-number="306"></td>
        <td id="LC306" class="blob-code js-file-line">					<span class="pl-s">var</span> subTreeReqURI <span class="pl-k">=</span> jdo.subTreeRequest(li, xPathToNode, evt, currState);</td>
      </tr>
      <tr>
        <td id="L307" class="blob-num js-line-number" data-line-number="307"></td>
        <td id="LC307" class="blob-code js-file-line">					<span class="pl-k">if</span> (subTreeReqURI <span class="pl-k">&amp;&amp;</span> <span class="pl-k">typeof</span> subTreeReqURI <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>string<span class="pl-pds">&#39;</span></span>) {</td>
      </tr>
      <tr>
        <td id="L308" class="blob-num js-line-number" data-line-number="308"></td>
        <td id="LC308" class="blob-code js-file-line">						<span class="pl-s">var</span> tree <span class="pl-k">=</span> <span class="pl-k">new</span> <span class="pl-en">XMLTree</span>($.extend(jdo, {fpath<span class="pl-k">:</span> subTreeReqURI, container<span class="pl-k">:</span> li}), <span class="pl-c1">true</span>);</td>
      </tr>
      <tr>
        <td id="L309" class="blob-num js-line-number" data-line-number="309"></td>
        <td id="LC309" class="blob-code js-file-line">						<span class="pl-k">if</span> (tree) li.<span class="pl-sc">data</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>subTreeDataFetched<span class="pl-pds">&#39;</span></span>, <span class="pl-c1">true</span>);</td>
      </tr>
      <tr>
        <td id="L310" class="blob-num js-line-number" data-line-number="310"></td>
        <td id="LC310" class="blob-code js-file-line">					}</td>
      </tr>
      <tr>
        <td id="L311" class="blob-num js-line-number" data-line-number="311"></td>
        <td id="LC311" class="blob-code js-file-line">				}</td>
      </tr>
      <tr>
        <td id="L312" class="blob-num js-line-number" data-line-number="312"></td>
        <td id="LC312" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L313" class="blob-num js-line-number" data-line-number="313"></td>
        <td id="LC313" class="blob-code js-file-line">				<span class="pl-c">//Flip plus/minus indicator and class</span></td>
      </tr>
      <tr>
        <td id="L314" class="blob-num js-line-number" data-line-number="314"></td>
        <td id="LC314" class="blob-code js-file-line">				$(<span class="pl-v">this</span>).html(currState <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>closed<span class="pl-pds">&#39;</span></span> <span class="pl-k">?</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>-<span class="pl-pds">&#39;</span></span> <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>+<span class="pl-pds">&#39;</span></span>).removeClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>expanded collapsed<span class="pl-pds">&#39;</span></span>).addClass(currState <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>closed<span class="pl-pds">&#39;</span></span> <span class="pl-k">?</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>expanded<span class="pl-pds">&#39;</span></span> <span class="pl-k">:</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>collapsed<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L315" class="blob-num js-line-number" data-line-number="315"></td>
        <td id="LC315" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L316" class="blob-num js-line-number" data-line-number="316"></td>
        <td id="LC316" class="blob-code js-file-line">				<span class="pl-c">//Log curr tree pos in URL hash, made up of comma-sep LI indexes of open ULs (LIs with multiple open ULs are sub-sep by -)</span></td>
      </tr>
      <tr>
        <td id="L317" class="blob-num js-line-number" data-line-number="317"></td>
        <td id="LC317" class="blob-code js-file-line">				<span class="pl-k">if</span> (<span class="pl-k">!</span>jdo.noURLTracking) {</td>
      </tr>
      <tr>
        <td id="L318" class="blob-num js-line-number" data-line-number="318"></td>
        <td id="LC318" class="blob-code js-file-line">					<span class="pl-s">var</span> paths <span class="pl-k">=</span> [];</td>
      </tr>
      <tr>
        <td id="L319" class="blob-num js-line-number" data-line-number="319"></td>
        <td id="LC319" class="blob-code js-file-line">					thiss.tree.<span class="pl-s3">find</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>ul:visible<span class="pl-pds">&#39;</span></span>).filter(<span class="pl-st">function</span>() { <span class="pl-k">return</span> <span class="pl-k">!</span>$(<span class="pl-v">this</span>).<span class="pl-s3">find</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>ul:visible<span class="pl-pds">&#39;</span></span>).<span class="pl-sc">length</span>; }).each(<span class="pl-st">function</span>() {</td>
      </tr>
      <tr>
        <td id="L320" class="blob-num js-line-number" data-line-number="320"></td>
        <td id="LC320" class="blob-code js-file-line">						<span class="pl-s">var</span> thisPathIndecies <span class="pl-k">=</span> [];</td>
      </tr>
      <tr>
        <td id="L321" class="blob-num js-line-number" data-line-number="321"></td>
        <td id="LC321" class="blob-code js-file-line">						$(<span class="pl-v">this</span>).parents(<span class="pl-s1"><span class="pl-pds">&#39;</span>li<span class="pl-pds">&#39;</span></span>).each(<span class="pl-st">function</span>() { thisPathIndecies.<span class="pl-s3">unshift</span>($(<span class="pl-v">this</span>).<span class="pl-sc">index</span>()); });</td>
      </tr>
      <tr>
        <td id="L322" class="blob-num js-line-number" data-line-number="322"></td>
        <td id="LC322" class="blob-code js-file-line">						paths.<span class="pl-s3">push</span>(thisPathIndecies.<span class="pl-s3">join</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>,<span class="pl-pds">&#39;</span></span>));</td>
      </tr>
      <tr>
        <td id="L323" class="blob-num js-line-number" data-line-number="323"></td>
        <td id="LC323" class="blob-code js-file-line">					});</td>
      </tr>
      <tr>
        <td id="L324" class="blob-num js-line-number" data-line-number="324"></td>
        <td id="LC324" class="blob-code js-file-line">					<span class="pl-s">var</span> newTreeHash <span class="pl-k">=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>tree<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>thiss.instanceID<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>:<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>paths.<span class="pl-s3">join</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>|<span class="pl-pds">&#39;</span></span>)<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>;<span class="pl-pds">&#39;</span></span></td>
      </tr>
      <tr>
        <td id="L325" class="blob-num js-line-number" data-line-number="325"></td>
        <td id="LC325" class="blob-code js-file-line">					<span class="pl-c">// idea by http://stackoverflow.com/a/5257214/873282</span></td>
      </tr>
      <tr>
        <td id="L326" class="blob-num js-line-number" data-line-number="326"></td>
        <td id="LC326" class="blob-code js-file-line">					<span class="pl-s">var</span> replaced <span class="pl-k">=</span> <span class="pl-c1">false</span>;</td>
      </tr>
      <tr>
        <td id="L327" class="blob-num js-line-number" data-line-number="327"></td>
        <td id="LC327" class="blob-code js-file-line">					<span class="pl-s">var</span> regExp <span class="pl-k">=</span> <span class="pl-k">new</span> <span class="pl-en">RegExp</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>tree<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>thiss.instanceID<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>:([0-9,<span class="pl-cce">\-\|</span>]+);<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L328" class="blob-num js-line-number" data-line-number="328"></td>
        <td id="LC328" class="blob-code js-file-line">					<span class="pl-s">var</span> newHash <span class="pl-k">=</span> location.<span class="pl-sc">hash</span>.<span class="pl-s3">replace</span>(regExp, <span class="pl-st">function</span>(<span class="pl-vpf">token</span>){replaced <span class="pl-k">=</span> <span class="pl-c1">true</span>; <span class="pl-k">return</span> newTreeHash;});</td>
      </tr>
      <tr>
        <td id="L329" class="blob-num js-line-number" data-line-number="329"></td>
        <td id="LC329" class="blob-code js-file-line">					<span class="pl-k">if</span> (<span class="pl-k">!</span>replaced) {</td>
      </tr>
      <tr>
        <td id="L330" class="blob-num js-line-number" data-line-number="330"></td>
        <td id="LC330" class="blob-code js-file-line">						<span class="pl-c">// no match, just append</span></td>
      </tr>
      <tr>
        <td id="L331" class="blob-num js-line-number" data-line-number="331"></td>
        <td id="LC331" class="blob-code js-file-line">						<span class="pl-k">if</span> (newHash.<span class="pl-s3">indexOf</span>(<span class="pl-s1"><span class="pl-pds">&quot;</span>;<span class="pl-pds">&quot;</span></span>, newHash.<span class="pl-sc">length</span> <span class="pl-k">-</span> <span class="pl-c1">1</span>) <span class="pl-k">!==</span> <span class="pl-k">-</span><span class="pl-c1">1</span>) {</td>
      </tr>
      <tr>
        <td id="L332" class="blob-num js-line-number" data-line-number="332"></td>
        <td id="LC332" class="blob-code js-file-line">							<span class="pl-c">// newHash ends with &quot;;&quot; -&gt; just append newTreeHash</span></td>
      </tr>
      <tr>
        <td id="L333" class="blob-num js-line-number" data-line-number="333"></td>
        <td id="LC333" class="blob-code js-file-line">							newHash <span class="pl-k">=</span> newHash <span class="pl-k">+</span> newTreeHash;</td>
      </tr>
      <tr>
        <td id="L334" class="blob-num js-line-number" data-line-number="334"></td>
        <td id="LC334" class="blob-code js-file-line">						} <span class="pl-k">else</span> {</td>
      </tr>
      <tr>
        <td id="L335" class="blob-num js-line-number" data-line-number="335"></td>
        <td id="LC335" class="blob-code js-file-line">							<span class="pl-c">// &quot;;&quot; needed as separator</span></td>
      </tr>
      <tr>
        <td id="L336" class="blob-num js-line-number" data-line-number="336"></td>
        <td id="LC336" class="blob-code js-file-line">							newHash <span class="pl-k">=</span> newHash <span class="pl-k">+</span> <span class="pl-s1"><span class="pl-pds">&quot;</span>;<span class="pl-pds">&quot;</span></span> <span class="pl-k">+</span> newTreeHash;</td>
      </tr>
      <tr>
        <td id="L337" class="blob-num js-line-number" data-line-number="337"></td>
        <td id="LC337" class="blob-code js-file-line">						}</td>
      </tr>
      <tr>
        <td id="L338" class="blob-num js-line-number" data-line-number="338"></td>
        <td id="LC338" class="blob-code js-file-line">					}</td>
      </tr>
      <tr>
        <td id="L339" class="blob-num js-line-number" data-line-number="339"></td>
        <td id="LC339" class="blob-code js-file-line">					location.<span class="pl-sc">hash</span> <span class="pl-k">=</span> newHash;</td>
      </tr>
      <tr>
        <td id="L340" class="blob-num js-line-number" data-line-number="340"></td>
        <td id="LC340" class="blob-code js-file-line">				}</td>
      </tr>
      <tr>
        <td id="L341" class="blob-num js-line-number" data-line-number="341"></td>
        <td id="LC341" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L342" class="blob-num js-line-number" data-line-number="342"></td>
        <td id="LC342" class="blob-code js-file-line">			})</td>
      </tr>
      <tr>
        <td id="L343" class="blob-num js-line-number" data-line-number="343"></td>
        <td id="LC343" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L344" class="blob-num js-line-number" data-line-number="344"></td>
        <td id="LC344" class="blob-code js-file-line">			<span class="pl-c">//do callback on click to actual nodes? Pass LI, LI&#39;s xPath and event obj.</span></td>
      </tr>
      <tr>
        <td id="L345" class="blob-num js-line-number" data-line-number="345"></td>
        <td id="LC345" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.clickCallback)</td>
      </tr>
      <tr>
        <td id="L346" class="blob-num js-line-number" data-line-number="346"></td>
        <td id="LC346" class="blob-code js-file-line">				<span class="pl-v">this</span>.tree.on(<span class="pl-s1"><span class="pl-pds">&#39;</span>click<span class="pl-pds">&#39;</span></span>, <span class="pl-s1"><span class="pl-pds">&#39;</span>.LIText<span class="pl-pds">&#39;</span></span>, <span class="pl-st">function</span>(<span class="pl-vpf">evt</span>) {</td>
      </tr>
      <tr>
        <td id="L347" class="blob-num js-line-number" data-line-number="347"></td>
        <td id="LC347" class="blob-code js-file-line">					<span class="pl-s">var</span> li <span class="pl-k">=</span> $(<span class="pl-v">this</span>).closest(<span class="pl-s1"><span class="pl-pds">&#39;</span>li<span class="pl-pds">&#39;</span></span>); jdo.clickCallback(li, returnXPathToNode(li), evt);</td>
      </tr>
      <tr>
        <td id="L348" class="blob-num js-line-number" data-line-number="348"></td>
        <td id="LC348" class="blob-code js-file-line">				});</td>
      </tr>
      <tr>
        <td id="L349" class="blob-num js-line-number" data-line-number="349"></td>
        <td id="LC349" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L350" class="blob-num js-line-number" data-line-number="350"></td>
        <td id="LC350" class="blob-code js-file-line">			<span class="pl-c">//hide attrs if params say so</span></td>
      </tr>
      <tr>
        <td id="L351" class="blob-num js-line-number" data-line-number="351"></td>
        <td id="LC351" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.hideAttrs <span class="pl-k">&amp;&amp;</span> <span class="pl-k">!</span>jdo.subTree) <span class="pl-v">this</span>.tree.addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>hideAttrs<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L352" class="blob-num js-line-number" data-line-number="352"></td>
        <td id="LC352" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L353" class="blob-num js-line-number" data-line-number="353"></td>
        <td id="LC353" class="blob-code js-file-line">			<span class="pl-c">//hide node names, if params say so</span></td>
      </tr>
      <tr>
        <td id="L354" class="blob-num js-line-number" data-line-number="354"></td>
        <td id="LC354" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.hideNodeNames <span class="pl-k">&amp;&amp;</span> <span class="pl-k">!</span>jdo.subTree) <span class="pl-v">this</span>.tree.addClass(<span class="pl-s1"><span class="pl-pds">&#39;</span>hideNodeNames<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L355" class="blob-num js-line-number" data-line-number="355"></td>
        <td id="LC355" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L356" class="blob-num js-line-number" data-line-number="356"></td>
        <td id="LC356" class="blob-code js-file-line">			<span class="pl-c">//render callback?</span></td>
      </tr>
      <tr>
        <td id="L357" class="blob-num js-line-number" data-line-number="357"></td>
        <td id="LC357" class="blob-code js-file-line">			<span class="pl-k">if</span> (jdo.renderCallback) jdo.renderCallback(<span class="pl-v">this</span>.tree, <span class="pl-v">this</span>, subTreeRequest);</td>
      </tr>
      <tr>
        <td id="L358" class="blob-num js-line-number" data-line-number="358"></td>
        <td id="LC358" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L359" class="blob-num js-line-number" data-line-number="359"></td>
        <td id="LC359" class="blob-code js-file-line">			<span class="pl-c">//onload - re-entry point(s) stipulated in URL hash or in params (@openAtPath)?</span></td>
      </tr>
      <tr>
        <td id="L360" class="blob-num js-line-number" data-line-number="360"></td>
        <td id="LC360" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L361" class="blob-num js-line-number" data-line-number="361"></td>
        <td id="LC361" class="blob-code js-file-line">			<span class="pl-c">//...stipulated in hash</span></td>
      </tr>
      <tr>
        <td id="L362" class="blob-num js-line-number" data-line-number="362"></td>
        <td id="LC362" class="blob-code js-file-line">			<span class="pl-s">var</span> paths <span class="pl-k">=</span> <span class="pl-k">new</span> <span class="pl-en">RegExp</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>tree<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span><span class="pl-v">this</span>.instanceID<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>:([0-9,<span class="pl-cce">\-\|</span>]+);<span class="pl-pds">&#39;</span></span>).<span class="pl-s3">exec</span>(location.<span class="pl-sc">hash</span>);</td>
      </tr>
      <tr>
        <td id="L363" class="blob-num js-line-number" data-line-number="363"></td>
        <td id="LC363" class="blob-code js-file-line">			<span class="pl-k">if</span> (paths) {</td>
      </tr>
      <tr>
        <td id="L364" class="blob-num js-line-number" data-line-number="364"></td>
        <td id="LC364" class="blob-code js-file-line">				<span class="pl-s">var</span> paths <span class="pl-k">=</span> paths[<span class="pl-c1">1</span>].<span class="pl-s3">split</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>|<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L365" class="blob-num js-line-number" data-line-number="365"></td>
        <td id="LC365" class="blob-code js-file-line">				<span class="pl-k">for</span>(<span class="pl-s">var</span> y <span class="pl-k">in</span> paths) {</td>
      </tr>
      <tr>
        <td id="L366" class="blob-num js-line-number" data-line-number="366"></td>
        <td id="LC366" class="blob-code js-file-line">					<span class="pl-s">var</span> parts <span class="pl-k">=</span> paths[y].<span class="pl-s3">split</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>,<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L367" class="blob-num js-line-number" data-line-number="367"></td>
        <td id="LC367" class="blob-code js-file-line">					<span class="pl-s">var</span> selStr <span class="pl-k">=</span> [];</td>
      </tr>
      <tr>
        <td id="L368" class="blob-num js-line-number" data-line-number="368"></td>
        <td id="LC368" class="blob-code js-file-line">					<span class="pl-k">for</span>(<span class="pl-s">var</span> i <span class="pl-k">in</span> parts) selStr.<span class="pl-s3">push</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>li:eq(<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>parts[i]<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>) &gt; ul<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L369" class="blob-num js-line-number" data-line-number="369"></td>
        <td id="LC369" class="blob-code js-file-line">					<span class="pl-v">this</span>.tree.<span class="pl-s3">find</span>(selStr.<span class="pl-s3">join</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span> &gt; <span class="pl-pds">&#39;</span></span>)).parents(<span class="pl-s1"><span class="pl-pds">&#39;</span>ul<span class="pl-pds">&#39;</span></span>).andSelf().show().each(<span class="pl-st">function</span>() {</td>
      </tr>
      <tr>
        <td id="L370" class="blob-num js-line-number" data-line-number="370"></td>
        <td id="LC370" class="blob-code js-file-line">						$(<span class="pl-v">this</span>).<span class="pl-sc">parent</span>().children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.plusMin<span class="pl-pds">&#39;</span></span>).html(<span class="pl-s1"><span class="pl-pds">&#39;</span>-<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L371" class="blob-num js-line-number" data-line-number="371"></td>
        <td id="LC371" class="blob-code js-file-line">					});</td>
      </tr>
      <tr>
        <td id="L372" class="blob-num js-line-number" data-line-number="372"></td>
        <td id="LC372" class="blob-code js-file-line">				}</td>
      </tr>
      <tr>
        <td id="L373" class="blob-num js-line-number" data-line-number="373"></td>
        <td id="LC373" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L374" class="blob-num js-line-number" data-line-number="374"></td>
        <td id="LC374" class="blob-code js-file-line">			<span class="pl-c">//...stipulated in params</span></td>
      </tr>
      <tr>
        <td id="L375" class="blob-num js-line-number" data-line-number="375"></td>
        <td id="LC375" class="blob-code js-file-line">			} <span class="pl-k">else</span></td>
      </tr>
      <tr>
        <td id="L376" class="blob-num js-line-number" data-line-number="376"></td>
        <td id="LC376" class="blob-code js-file-line">				<span class="pl-v">this</span>.tree.<span class="pl-s3">find</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>.currSel<span class="pl-pds">&#39;</span></span>).parentsUntil(<span class="pl-s1"><span class="pl-pds">&#39;</span>.xmltree<span class="pl-pds">&#39;</span></span>).show();</td>
      </tr>
      <tr>
        <td id="L377" class="blob-num js-line-number" data-line-number="377"></td>
        <td id="LC377" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L378" class="blob-num js-line-number" data-line-number="378"></td>
        <td id="LC378" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L379" class="blob-num js-line-number" data-line-number="379"></td>
        <td id="LC379" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L380" class="blob-num js-line-number" data-line-number="380"></td>
        <td id="LC380" class="blob-code js-file-line">		<span class="pl-c">//return tree, in case it was assigned</span></td>
      </tr>
      <tr>
        <td id="L381" class="blob-num js-line-number" data-line-number="381"></td>
        <td id="LC381" class="blob-code js-file-line">		<span class="pl-k">return</span> <span class="pl-v">this</span>.tree;</td>
      </tr>
      <tr>
        <td id="L382" class="blob-num js-line-number" data-line-number="382"></td>
        <td id="LC382" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L383" class="blob-num js-line-number" data-line-number="383"></td>
        <td id="LC383" class="blob-code js-file-line">	}</td>
      </tr>
      <tr>
        <td id="L384" class="blob-num js-line-number" data-line-number="384"></td>
        <td id="LC384" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L385" class="blob-num js-line-number" data-line-number="385"></td>
        <td id="LC385" class="blob-code js-file-line">	<span class="pl-c">//count instances. Used for assigning unique IDs</span></td>
      </tr>
      <tr>
        <td id="L386" class="blob-num js-line-number" data-line-number="386"></td>
        <td id="LC386" class="blob-code js-file-line">	XMLTree.instancesCounter <span class="pl-k">=</span> <span class="pl-c1">0</span>;</td>
      </tr>
      <tr>
        <td id="L387" class="blob-num js-line-number" data-line-number="387"></td>
        <td id="LC387" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L388" class="blob-num js-line-number" data-line-number="388"></td>
        <td id="LC388" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L389" class="blob-num js-line-number" data-line-number="389"></td>
        <td id="LC389" class="blob-code js-file-line">	<span class="pl-c">/* -------------------</span></td>
      </tr>
      <tr>
        <td id="L390" class="blob-num js-line-number" data-line-number="390"></td>
        <td id="LC390" class="blob-code js-file-line"><span class="pl-c">	| UTILS</span></td>
      </tr>
      <tr>
        <td id="L391" class="blob-num js-line-number" data-line-number="391"></td>
        <td id="LC391" class="blob-code js-file-line"><span class="pl-c">	------------------- */</span></td>
      </tr>
      <tr>
        <td id="L392" class="blob-num js-line-number" data-line-number="392"></td>
        <td id="LC392" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L393" class="blob-num js-line-number" data-line-number="393"></td>
        <td id="LC393" class="blob-code js-file-line">	<span class="pl-c">//parse XML</span></td>
      </tr>
      <tr>
        <td id="L394" class="blob-num js-line-number" data-line-number="394"></td>
        <td id="LC394" class="blob-code js-file-line">	<span class="pl-st">function</span> <span class="pl-en">parseXML</span>(<span class="pl-vpf">XMLStr</span>) {</td>
      </tr>
      <tr>
        <td id="L395" class="blob-num js-line-number" data-line-number="395"></td>
        <td id="LC395" class="blob-code js-file-line">		<span class="pl-k">if</span> (<span class="pl-s3">window</span>.DOMParser) {</td>
      </tr>
      <tr>
        <td id="L396" class="blob-num js-line-number" data-line-number="396"></td>
        <td id="LC396" class="blob-code js-file-line">			parser<span class="pl-k">=</span><span class="pl-k">new</span> <span class="pl-en">DOMParser</span>();</td>
      </tr>
      <tr>
        <td id="L397" class="blob-num js-line-number" data-line-number="397"></td>
        <td id="LC397" class="blob-code js-file-line">			xmlDoc<span class="pl-k">=</span>parser.parseFromString(XMLStr,<span class="pl-s1"><span class="pl-pds">&quot;</span>text/xml<span class="pl-pds">&quot;</span></span>);</td>
      </tr>
      <tr>
        <td id="L398" class="blob-num js-line-number" data-line-number="398"></td>
        <td id="LC398" class="blob-code js-file-line">		} <span class="pl-k">else</span> {</td>
      </tr>
      <tr>
        <td id="L399" class="blob-num js-line-number" data-line-number="399"></td>
        <td id="LC399" class="blob-code js-file-line">			xmlDoc<span class="pl-k">=</span><span class="pl-k">new</span> <span class="pl-en">ActiveXObject</span>(<span class="pl-s1"><span class="pl-pds">&quot;</span>Microsoft.XMLDOM<span class="pl-pds">&quot;</span></span>);</td>
      </tr>
      <tr>
        <td id="L400" class="blob-num js-line-number" data-line-number="400"></td>
        <td id="LC400" class="blob-code js-file-line">			xmlDoc.async<span class="pl-k">=</span><span class="pl-c1">false</span>;</td>
      </tr>
      <tr>
        <td id="L401" class="blob-num js-line-number" data-line-number="401"></td>
        <td id="LC401" class="blob-code js-file-line">			xmlDoc.loadXML(XMLStr);</td>
      </tr>
      <tr>
        <td id="L402" class="blob-num js-line-number" data-line-number="402"></td>
        <td id="LC402" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L403" class="blob-num js-line-number" data-line-number="403"></td>
        <td id="LC403" class="blob-code js-file-line">		<span class="pl-k">return</span> xmlDoc;</td>
      </tr>
      <tr>
        <td id="L404" class="blob-num js-line-number" data-line-number="404"></td>
        <td id="LC404" class="blob-code js-file-line">	}</td>
      </tr>
      <tr>
        <td id="L405" class="blob-num js-line-number" data-line-number="405"></td>
        <td id="LC405" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L406" class="blob-num js-line-number" data-line-number="406"></td>
        <td id="LC406" class="blob-code js-file-line">	<span class="pl-c">//get immediate text</span></td>
      </tr>
      <tr>
        <td id="L407" class="blob-num js-line-number" data-line-number="407"></td>
        <td id="LC407" class="blob-code js-file-line">	<span class="pl-s3">$.fn</span>.<span class="pl-en">immediateText</span> <span class="pl-k">=</span> <span class="pl-st">function</span>() { <span class="pl-k">return</span> $(<span class="pl-v">this</span>).clone().children().<span class="pl-s3">remove</span>().end().<span class="pl-sc">text</span>(); }</td>
      </tr>
      <tr>
        <td id="L408" class="blob-num js-line-number" data-line-number="408"></td>
        <td id="LC408" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L409" class="blob-num js-line-number" data-line-number="409"></td>
        <td id="LC409" class="blob-code js-file-line">	<span class="pl-c">//XPath - return XPath of clicked node</span></td>
      </tr>
      <tr>
        <td id="L410" class="blob-num js-line-number" data-line-number="410"></td>
        <td id="LC410" class="blob-code js-file-line">	<span class="pl-st">function</span> <span class="pl-en">returnXPathToNode</span>(<span class="pl-vpf">nodeEl</span>) {</td>
      </tr>
      <tr>
        <td id="L411" class="blob-num js-line-number" data-line-number="411"></td>
        <td id="LC411" class="blob-code js-file-line">		<span class="pl-s">var</span> path <span class="pl-k">=</span> [];</td>
      </tr>
      <tr>
        <td id="L412" class="blob-num js-line-number" data-line-number="412"></td>
        <td id="LC412" class="blob-code js-file-line">		nodeEl.parents(<span class="pl-s1"><span class="pl-pds">&#39;</span>li<span class="pl-pds">&#39;</span></span>).andSelf().each(<span class="pl-st">function</span>() {</td>
      </tr>
      <tr>
        <td id="L413" class="blob-num js-line-number" data-line-number="413"></td>
        <td id="LC413" class="blob-code js-file-line">			<span class="pl-s">var</span> nodeName <span class="pl-k">=</span> $(<span class="pl-v">this</span>).children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.LIText<span class="pl-pds">&#39;</span></span>).children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.tree_node<span class="pl-pds">&#39;</span></span>).<span class="pl-sc">text</span>();</td>
      </tr>
      <tr>
        <td id="L414" class="blob-num js-line-number" data-line-number="414"></td>
        <td id="LC414" class="blob-code js-file-line">			<span class="pl-s">var</span> step <span class="pl-k">=</span> nodeName;</td>
      </tr>
      <tr>
        <td id="L415" class="blob-num js-line-number" data-line-number="415"></td>
        <td id="LC415" class="blob-code js-file-line">			<span class="pl-c">// The li element has the nodename attached as class.</span></td>
      </tr>
      <tr>
        <td id="L416" class="blob-num js-line-number" data-line-number="416"></td>
        <td id="LC416" class="blob-code js-file-line">			<span class="pl-c">// If nodes have a QName with a namespace prefix, that class cannot be queried</span></td>
      </tr>
      <tr>
        <td id="L417" class="blob-num js-line-number" data-line-number="417"></td>
        <td id="LC417" class="blob-code js-file-line">			<span class="pl-c">// Therefore, we have to check the grandchild of each li element</span></td>
      </tr>
      <tr>
        <td id="L418" class="blob-num js-line-number" data-line-number="418"></td>
        <td id="LC418" class="blob-code js-file-line">			<span class="pl-s">var</span> numberOfSilblings <span class="pl-k">=</span> $(<span class="pl-v">this</span>).siblings().filter(<span class="pl-st">function</span>() { <span class="pl-k">return</span> $(<span class="pl-v">this</span>).children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.LIText<span class="pl-pds">&#39;</span></span>).children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.tree_node<span class="pl-pds">&#39;</span></span>).<span class="pl-sc">text</span>() <span class="pl-k">==</span> nodeName; }).<span class="pl-sc">length</span>;</td>
      </tr>
      <tr>
        <td id="L419" class="blob-num js-line-number" data-line-number="419"></td>
        <td id="LC419" class="blob-code js-file-line">			<span class="pl-k">if</span> (numberOfSilblings <span class="pl-k">!=</span> <span class="pl-c1">0</span>) {</td>
      </tr>
      <tr>
        <td id="L420" class="blob-num js-line-number" data-line-number="420"></td>
        <td id="LC420" class="blob-code js-file-line">				<span class="pl-c">// more than one child with the same name -- we have to add an index</span></td>
      </tr>
      <tr>
        <td id="L421" class="blob-num js-line-number" data-line-number="421"></td>
        <td id="LC421" class="blob-code js-file-line">				<span class="pl-s">var</span> index <span class="pl-k">=</span> $(<span class="pl-v">this</span>).prevAll().filter(<span class="pl-st">function</span>() { <span class="pl-k">return</span> $(<span class="pl-v">this</span>).children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.LIText<span class="pl-pds">&#39;</span></span>).children(<span class="pl-s1"><span class="pl-pds">&#39;</span>.tree_node<span class="pl-pds">&#39;</span></span>).<span class="pl-sc">text</span>() <span class="pl-k">==</span> nodeName; }).<span class="pl-sc">length</span> <span class="pl-k">+</span> <span class="pl-c1">1</span>;</td>
      </tr>
      <tr>
        <td id="L422" class="blob-num js-line-number" data-line-number="422"></td>
        <td id="LC422" class="blob-code js-file-line">				step <span class="pl-k">+=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>[<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>index<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>]<span class="pl-pds">&#39;</span></span>;</td>
      </tr>
      <tr>
        <td id="L423" class="blob-num js-line-number" data-line-number="423"></td>
        <td id="LC423" class="blob-code js-file-line">			}</td>
      </tr>
      <tr>
        <td id="L424" class="blob-num js-line-number" data-line-number="424"></td>
        <td id="LC424" class="blob-code js-file-line">			path.<span class="pl-s3">push</span>(step);</td>
      </tr>
      <tr>
        <td id="L425" class="blob-num js-line-number" data-line-number="425"></td>
        <td id="LC425" class="blob-code js-file-line">		 });</td>
      </tr>
      <tr>
        <td id="L426" class="blob-num js-line-number" data-line-number="426"></td>
        <td id="LC426" class="blob-code js-file-line">		<span class="pl-k">return</span> <span class="pl-s1"><span class="pl-pds">&quot;</span>/<span class="pl-pds">&quot;</span></span> <span class="pl-k">+</span> path.<span class="pl-s3">join</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span>/<span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L427" class="blob-num js-line-number" data-line-number="427"></td>
        <td id="LC427" class="blob-code js-file-line">	}</td>
      </tr>
      <tr>
        <td id="L428" class="blob-num js-line-number" data-line-number="428"></td>
        <td id="LC428" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L429" class="blob-num js-line-number" data-line-number="429"></td>
        <td id="LC429" class="blob-code js-file-line">	<span class="pl-c">//debug (console.log)</span></td>
      </tr>
      <tr>
        <td id="L430" class="blob-num js-line-number" data-line-number="430"></td>
        <td id="LC430" class="blob-code js-file-line">	<span class="pl-st">function</span> <span class="pl-en">debug</span>() { <span class="pl-k">if</span> (<span class="pl-s3">window</span>.<span class="pl-en">console</span> <span class="pl-k">&amp;&amp;</span> <span class="pl-en">console</span><span class="pl-s3">.log</span> <span class="pl-k">&amp;&amp;</span> log) <span class="pl-k">for</span> (<span class="pl-s">var</span> i <span class="pl-k">in</span> arguments) <span class="pl-en">console</span><span class="pl-s3">.log</span>(arguments[i]); }</td>
      </tr>
      <tr>
        <td id="L431" class="blob-num js-line-number" data-line-number="431"></td>
        <td id="LC431" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L432" class="blob-num js-line-number" data-line-number="432"></td>
        <td id="LC432" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L433" class="blob-num js-line-number" data-line-number="433"></td>
        <td id="LC433" class="blob-code js-file-line">	<span class="pl-c">/* ---</span></td>
      </tr>
      <tr>
        <td id="L434" class="blob-num js-line-number" data-line-number="434"></td>
        <td id="LC434" class="blob-code js-file-line"><span class="pl-c">	| JSON &gt; XML convertor (creates XML string)</span></td>
      </tr>
      <tr>
        <td id="L435" class="blob-num js-line-number" data-line-number="435"></td>
        <td id="LC435" class="blob-code js-file-line"><span class="pl-c">	--- */</span></td>
      </tr>
      <tr>
        <td id="L436" class="blob-num js-line-number" data-line-number="436"></td>
        <td id="LC436" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L437" class="blob-num js-line-number" data-line-number="437"></td>
        <td id="LC437" class="blob-code js-file-line">	<span class="pl-st">function</span> <span class="pl-en">json_to_xml</span>(<span class="pl-vpf">obj</span>, <span class="pl-vpf">root_name</span>, <span class="pl-vpf">depth</span>) {</td>
      </tr>
      <tr>
        <td id="L438" class="blob-num js-line-number" data-line-number="438"></td>
        <td id="LC438" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L439" class="blob-num js-line-number" data-line-number="439"></td>
        <td id="LC439" class="blob-code js-file-line">		<span class="pl-c">//prep</span></td>
      </tr>
      <tr>
        <td id="L440" class="blob-num js-line-number" data-line-number="440"></td>
        <td id="LC440" class="blob-code js-file-line">		<span class="pl-s">var</span> xml <span class="pl-k">=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-pds">&#39;</span></span>, depth <span class="pl-k">=</span> depth <span class="pl-k">||</span> <span class="pl-c1">0</span>, root_name <span class="pl-k">=</span> root_name <span class="pl-k">||</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>root<span class="pl-pds">&#39;</span></span>;</td>
      </tr>
      <tr>
        <td id="L441" class="blob-num js-line-number" data-line-number="441"></td>
        <td id="LC441" class="blob-code js-file-line">		<span class="pl-k">if</span> (<span class="pl-k">!</span>depth) xml <span class="pl-k">=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>root_name<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>&gt;<span class="pl-pds">&#39;</span></span>;</td>
      </tr>
      <tr>
        <td id="L442" class="blob-num js-line-number" data-line-number="442"></td>
        <td id="LC442" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L443" class="blob-num js-line-number" data-line-number="443"></td>
        <td id="LC443" class="blob-code js-file-line">		<span class="pl-c">//recurse over passed object (for-in) or array (for)</span></td>
      </tr>
      <tr>
        <td id="L444" class="blob-num js-line-number" data-line-number="444"></td>
        <td id="LC444" class="blob-code js-file-line">		<span class="pl-k">if</span> (obj.<span class="pl-s3">toString</span>() <span class="pl-k">==</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>[object Object]<span class="pl-pds">&#39;</span></span>) <span class="pl-k">for</span> (<span class="pl-s">var</span> i <span class="pl-k">in</span> obj) xml <span class="pl-k">+=</span> build_node(i, obj[i]);</td>
      </tr>
      <tr>
        <td id="L445" class="blob-num js-line-number" data-line-number="445"></td>
        <td id="LC445" class="blob-code js-file-line">		<span class="pl-k">else</span> <span class="pl-k">if</span> (obj <span class="pl-k">instanceof</span> <span class="pl-s3">Array</span>) <span class="pl-k">for</span> (<span class="pl-s">var</span> i<span class="pl-k">=</span><span class="pl-c1">0</span>, len <span class="pl-k">=</span> obj.<span class="pl-sc">length</span>; i<span class="pl-k">&lt;</span>len; i<span class="pl-k">++</span>) xml <span class="pl-k">+=</span> build_node(<span class="pl-s1"><span class="pl-pds">&#39;</span>node<span class="pl-pds">&#39;</span></span>, obj[i]);</td>
      </tr>
      <tr>
        <td id="L446" class="blob-num js-line-number" data-line-number="446"></td>
        <td id="LC446" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L447" class="blob-num js-line-number" data-line-number="447"></td>
        <td id="LC447" class="blob-code js-file-line">		<span class="pl-c">//util to build individual XML node. Tags named after object key or, if array, &#39;node&#39;. Coerce tag name to be valid.</span></td>
      </tr>
      <tr>
        <td id="L448" class="blob-num js-line-number" data-line-number="448"></td>
        <td id="LC448" class="blob-code js-file-line">		<span class="pl-st">function</span> <span class="pl-en">build_node</span>(<span class="pl-vpf">tag_name</span>, <span class="pl-vpf">val</span>) {</td>
      </tr>
      <tr>
        <td id="L449" class="blob-num js-line-number" data-line-number="449"></td>
        <td id="LC449" class="blob-code js-file-line">			<span class="pl-s">var</span></td>
      </tr>
      <tr>
        <td id="L450" class="blob-num js-line-number" data-line-number="450"></td>
        <td id="LC450" class="blob-code js-file-line">			tag_name <span class="pl-k">=</span> tag_name.<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span><span class="pl-c1">[<span class="pl-k">^</span><span class="pl-c1">\w</span><span class="pl-c1">\-_</span>]</span><span class="pl-pds">/</span>g</span>, <span class="pl-s1"><span class="pl-pds">&#39;</span>-<span class="pl-pds">&#39;</span></span>).<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span>-<span class="pl-k">{2,}</span><span class="pl-pds">/</span>g</span>, <span class="pl-s1"><span class="pl-pds">&#39;</span>-<span class="pl-pds">&#39;</span></span>).<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span><span class="pl-k">^</span><span class="pl-c1">[<span class="pl-k">^</span><span class="pl-c1">a-z</span>]</span><span class="pl-pds">/</span></span>, <span class="pl-st">function</span>(<span class="pl-vpf">$0</span>) { <span class="pl-k">return</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>node-<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>$<span class="pl-c1">0</span>; }),</td>
      </tr>
      <tr>
        <td id="L451" class="blob-num js-line-number" data-line-number="451"></td>
        <td id="LC451" class="blob-code js-file-line">			padder <span class="pl-k">=</span> <span class="pl-k">new</span> <span class="pl-en">Array</span>(depth <span class="pl-k">+</span> <span class="pl-c1">2</span>).<span class="pl-s3">join</span>(<span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-cce">\t</span><span class="pl-pds">&#39;</span></span>),</td>
      </tr>
      <tr>
        <td id="L452" class="blob-num js-line-number" data-line-number="452"></td>
        <td id="LC452" class="blob-code js-file-line">			node <span class="pl-k">=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-cce">\n</span><span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>padder<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>tag_name<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>&gt;<span class="pl-cce">\n</span><span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>padder<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-cce">\t</span><span class="pl-pds">&#39;</span></span>;</td>
      </tr>
      <tr>
        <td id="L453" class="blob-num js-line-number" data-line-number="453"></td>
        <td id="LC453" class="blob-code js-file-line">			node <span class="pl-k">+=</span> <span class="pl-k">typeof</span> val <span class="pl-k">!=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>object<span class="pl-pds">&#39;</span></span> <span class="pl-k">?</span> val <span class="pl-k">:</span> json_to_xml(val, <span class="pl-c1">null</span>, depth <span class="pl-k">+</span> <span class="pl-c1">1</span>);</td>
      </tr>
      <tr>
        <td id="L454" class="blob-num js-line-number" data-line-number="454"></td>
        <td id="LC454" class="blob-code js-file-line">			<span class="pl-k">return</span> node <span class="pl-k">+</span> <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-cce">\n</span><span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>padder<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;/<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>tag_name<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>&gt;<span class="pl-cce">\n</span><span class="pl-pds">&#39;</span></span>;</td>
      </tr>
      <tr>
        <td id="L455" class="blob-num js-line-number" data-line-number="455"></td>
        <td id="LC455" class="blob-code js-file-line">		}</td>
      </tr>
      <tr>
        <td id="L456" class="blob-num js-line-number" data-line-number="456"></td>
        <td id="LC456" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L457" class="blob-num js-line-number" data-line-number="457"></td>
        <td id="LC457" class="blob-code js-file-line">		<span class="pl-k">if</span> (<span class="pl-k">!</span>depth) xml <span class="pl-k">+=</span> <span class="pl-s1"><span class="pl-pds">&#39;</span>&lt;/<span class="pl-pds">&#39;</span></span><span class="pl-k">+</span>root_name<span class="pl-k">+</span><span class="pl-s1"><span class="pl-pds">&#39;</span>&gt;<span class="pl-pds">&#39;</span></span>;</td>
      </tr>
      <tr>
        <td id="L458" class="blob-num js-line-number" data-line-number="458"></td>
        <td id="LC458" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L459" class="blob-num js-line-number" data-line-number="459"></td>
        <td id="LC459" class="blob-code js-file-line">		<span class="pl-c">//return XML string, cleaning it up a bit first</span></td>
      </tr>
      <tr>
        <td id="L460" class="blob-num js-line-number" data-line-number="460"></td>
        <td id="LC460" class="blob-code js-file-line">		<span class="pl-k">return</span> xml</td>
      </tr>
      <tr>
        <td id="L461" class="blob-num js-line-number" data-line-number="461"></td>
        <td id="LC461" class="blob-code js-file-line">			.<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span>&amp;(?= )<span class="pl-pds">/</span>g</span>, <span class="pl-s1"><span class="pl-pds">&#39;</span>&amp;amp;<span class="pl-pds">&#39;</span></span>)</td>
      </tr>
      <tr>
        <td id="L462" class="blob-num js-line-number" data-line-number="462"></td>
        <td id="LC462" class="blob-code js-file-line">			.<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span><span class="pl-k">^</span><span class="pl-cce">\n</span>(?=&lt;)<span class="pl-pds">/</span></span>, <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-pds">&#39;</span></span>)</td>
      </tr>
      <tr>
        <td id="L463" class="blob-num js-line-number" data-line-number="463"></td>
        <td id="LC463" class="blob-code js-file-line">			.<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span><span class="pl-cce">\n</span><span class="pl-k">{2,}</span><span class="pl-pds">/</span>g</span>, <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-cce">\n</span><span class="pl-pds">&#39;</span></span>)</td>
      </tr>
      <tr>
        <td id="L464" class="blob-num js-line-number" data-line-number="464"></td>
        <td id="LC464" class="blob-code js-file-line">			.<span class="pl-s3">replace</span>(<span class="pl-sr"><span class="pl-pds">/</span><span class="pl-k">^</span><span class="pl-cce">\t</span><span class="pl-k">+</span><span class="pl-cce">\n</span><span class="pl-pds">/</span>mg</span>, <span class="pl-s1"><span class="pl-pds">&#39;</span><span class="pl-pds">&#39;</span></span>);</td>
      </tr>
      <tr>
        <td id="L465" class="blob-num js-line-number" data-line-number="465"></td>
        <td id="LC465" class="blob-code js-file-line">	}</td>
      </tr>
      <tr>
        <td id="L466" class="blob-num js-line-number" data-line-number="466"></td>
        <td id="LC466" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L467" class="blob-num js-line-number" data-line-number="467"></td>
        <td id="LC467" class="blob-code js-file-line">	<span class="pl-k">return</span> XMLTree;</td>
      </tr>
      <tr>
        <td id="L468" class="blob-num js-line-number" data-line-number="468"></td>
        <td id="LC468" class="blob-code js-file-line">}));</td>
      </tr>
</table>

  </div>

  </div>
</div>

<a href="#jump-to-line" rel="facebox[.linejump]" data-hotkey="l" style="display:none">Jump to Line</a>
<div id="jump-to-line" style="display:none">
  <form accept-charset="UTF-8" class="js-jump-to-line-form">
    <input class="linejump-input js-jump-to-line-field" type="text" placeholder="Jump to line&hellip;" autofocus>
    <button type="submit" class="button">Go</button>
  </form>
</div>

        </div>

      </div><!-- /.repo-container -->
      <div class="modal-backdrop"></div>
    </div><!-- /.container -->
  </div><!-- /.site -->


    </div><!-- /.wrapper -->

      <div class="container">
  <div class="site-footer" role="contentinfo">
    <ul class="site-footer-links right">
      <li><a href="https://status.github.com/">Status</a></li>
      <li><a href="https://developer.github.com">API</a></li>
      <li><a href="http://training.github.com">Training</a></li>
      <li><a href="http://shop.github.com">Shop</a></li>
      <li><a href="/blog">Blog</a></li>
      <li><a href="/about">About</a></li>

    </ul>

    <a href="/" aria-label="Homepage">
      <span class="mega-octicon octicon-mark-github" title="GitHub"></span>
    </a>

    <ul class="site-footer-links">
      <li>&copy; 2015 <span title="0.06749s from github-fe140-cp1-prd.iad.github.net">GitHub</span>, Inc.</li>
        <li><a href="/site/terms">Terms</a></li>
        <li><a href="/site/privacy">Privacy</a></li>
        <li><a href="/security">Security</a></li>
        <li><a href="/contact">Contact</a></li>
    </ul>
  </div><!-- /.site-footer -->
</div><!-- /.container -->


    <div class="fullscreen-overlay js-fullscreen-overlay" id="fullscreen_overlay">
  <div class="fullscreen-container js-suggester-container">
    <div class="textarea-wrap">
      <textarea name="fullscreen-contents" id="fullscreen-contents" class="fullscreen-contents js-fullscreen-contents" placeholder=""></textarea>
      <div class="suggester-container">
        <div class="suggester fullscreen-suggester js-suggester js-navigation-container"></div>
      </div>
    </div>
  </div>
  <div class="fullscreen-sidebar">
    <a href="#" class="exit-fullscreen js-exit-fullscreen tooltipped tooltipped-w" aria-label="Exit Zen Mode">
      <span class="mega-octicon octicon-screen-normal"></span>
    </a>
    <a href="#" class="theme-switcher js-theme-switcher tooltipped tooltipped-w"
      aria-label="Switch themes">
      <span class="octicon octicon-color-mode"></span>
    </a>
  </div>
</div>



    <div id="ajax-error-message" class="flash flash-error">
      <span class="octicon octicon-alert"></span>
      <a href="#" class="octicon octicon-x flash-close js-ajax-error-dismiss" aria-label="Dismiss error"></a>
      Something went wrong with that request. Please try again.
    </div>


      <script crossorigin="anonymous" src="https://assets-cdn.github.com/assets/frameworks-2a688ea200a50a474783d34f1f46405acbc687c4d17db98b4b2b769b44174d5d.js" type="text/javascript"></script>
      <script async="async" crossorigin="anonymous" src="https://assets-cdn.github.com/assets/github-7d7db0fa0812858bcfb8940e3c6babccd2e6f6321279090f48129acd6c2aaa3d.js" type="text/javascript"></script>
      
      
  </body>
</html>

