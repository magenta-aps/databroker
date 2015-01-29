
$.fn.extend({
    magentaTree: function(data) {

        // Receive plain json data, and generate data in a format that jsTree likes
        var processNode = function(name, node, hrefLinkDepth) {
            var outputNodes = [];
            var doProcessNode = function(name, node, hrefLinkDepth, index) {
                if (!name) {
                    if ($.isPlainObject(node)) {
                        name = node.name || node.navn || node.vejnavn || node.desc || null;
                    }
                    if (!name && index !== undefined) {
                        name = "" + index;
                    }
                }
                if (!hrefLinkDepth || hrefLinkDepth < 0) {
                    hrefLinkDepth = 0;
                }
                var output = null;
                if ($.isArray(node)) {
                    var children = [];
                    for (var i = 0; i < node.length; i++) {
                        children.push(doProcessNode(null, node[i], hrefLinkDepth-1, i));
                    }
                    // Each array or hash will generate a simple object with a text and an array of children
                    output = {
                        text: name,
                        children: children
                    };
                } else if ($.isPlainObject(node)) {
                    var children = [];
                    for (var key in node) {
                        children.push(doProcessNode(key, node[key], hrefLinkDepth-1));
                    }
                    output = {
                        text: name,
                        children: children
                    };
                } else if (typeof(node) == "string" || typeof(node) == "number" || typeof(node) == "boolean") {
                    // Value items will be styled as key: value, with a possible href class
                    // This href class determines whether a value is clickable (to get new server data)
                    var extraClass = (name == "href" && hrefLinkDepth == 0) ? "href" : "";
                    output = {
                        text: "<span class='key'>" + name + ": </span><span class='value " + extraClass + "'>" + node + "</span>",
                        key: name,
                        value: node
                    };
                } else {
                    console.log("node type not found", node)
                }
                if (output) {
                    output.nodeId = outputNodes.length;
                    outputNodes[output.nodeId] = output;
                    return output;
                }
            };

            return doProcessNode(name, node, hrefLinkDepth);
        }


        var self = this;
        // For all href-classed items in the tree, fetch the appropriate resource and handle it.
        this.delegate(".value.href", "click", function(){
            var el = $(this),
                url = el.text(),
                jstree = self.jstree(true),
                node = jstree.get_node(el),
                parentNode = jstree.get_node(node.parent);
            $.ajax({
                type: "GET",
                url: url,
                dataType: "json",
                success: function (responseJson, status, response) {
                    var nodeId = parentNode.original.nodeId;
                    newNode = processNode(null,responseJson,2); // Generate a new nodeset from out input, making sure that any href key in the current object doesn't keep the href class

                    // Do not add items that are already present in the object; get a list of their keys
                    var oldKeys = {};
                    for (var i=0; i<parentNode.children.length; i++) {
                        var oldChild = jstree.get_node(parentNode.children[i]);
                        var key = oldChild.original.key || (oldChild.children && oldChild.original.text) || null;
                        if (key) {
                            oldKeys[key] = oldChild;
                        }
                    }

                    // Update or create items, determined by the generated list
                    for (var i=0; i<newNode.children.length; i++) {
                        var newChild = newNode.children[i];
                        var key = newChild.key || newChild.text;
                        if (key in oldKeys) {
                            oldKeys[key].text = newChild.text;
                        } else {
                            jstree.create_node(parentNode, newChild);
                        }
                    }
                    // Redraw the tree
                    jstree.redraw(true);
                }
            });
        });





        //---------------------------------

        // Destroy any old trees for this container
        if ($.jstree.reference(this)) {
            this.jstree(true).destroy();
        }

        if (typeof(data) == "string") {
            data = $.parseJSON(data);
        }

        // Create and feed an input structure for jsTree
        // First, process our input
        var treeData = processNode("data",data,4);

        // Cut off the root node if it has only one child
        if (treeData.children.length == 1) {
            treeData.text = treeData.children[0].text;
            treeData.children = treeData.children[0].children;
        }

        // jsTree configuration; the first level is opened, the rest closed
        treeData.state = {opened:true,selected:true};


        var tree = this.jstree({
            core:{
                themes : {
                    icons: false // no icons
                },
                check_callback: true, // allow tree modification
                data: treeData // Our huge data structure
            },
            plugins : [
                "wholerow" // Fill the available width
            ]
        });
    }
});